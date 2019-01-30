import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import kotlinx.serialization.parse
import org.dom4j.DocumentHelper
import org.dom4j.QName
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.ScanRequest
import software.amazon.awssdk.services.lambda.LambdaClient
import software.amazon.awssdk.services.lambda.model.InvokeRequest
import software.amazon.awssdk.services.polly.PollyClient
import software.amazon.awssdk.services.polly.model.OutputFormat
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest
import software.amazon.awssdk.services.polly.model.TextType
import software.amazon.awssdk.services.polly.model.VoiceId
import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import java.lang.Exception
import java.nio.file.FileSystems
import java.text.SimpleDateFormat
import java.util.*

internal fun paroli(arbo: SintaksoArbo): Unit {
    val polly = PollyClient.builder()
            .region(Region.US_WEST_2)
            .build()
    val xml = DocumentHelper.createDocument()
    val xmlRadiko = xml.addElement("speak")
            .addElement(QName("amazon:effect"))
            .addAttribute("vocal-tract-length", "+7%")
            .addElement("prosody")
            .addAttribute("rate", "70%")
            .addAttribute("volume", "+6dB")
    val vortoj = mutableListOf<String>()
    arbo.traversi(kunPaŭzoj = true).forEach {
        if (it.startsWith("!")) {
            val ipa = igiIPA(vortoj.joinToString(" "))
            xmlRadiko.addElement("phoneme")
                    .addAttribute("alphabet", "ipa")
                    .addAttribute("ph", pravigiIPA(vortoj, ipa).joinToString(" "))
            when (it) {
                "!longapaŭzo" -> xmlRadiko.addElement("break")
                "!paŭzo" -> xmlRadiko.addElement("break").addAttribute("strength", "weak")
            }
            vortoj.clear()
        } else {
            vortoj.add(it)
        }
    }
    if (vortoj.isNotEmpty()) {
        val ipa = igiIPA(vortoj.joinToString(" "))
        xmlRadiko.addElement("phoneme")
                .addAttribute("alphabet", "ipa")
                .addAttribute("ph", pravigiIPA(vortoj, ipa).joinToString(" "))
    }
    val petoXML = xml.asXML()
    println(petoXML)
    polly.synthesizeSpeech(SynthesizeSpeechRequest.builder()
            .outputFormat(OutputFormat.OGG_VORBIS)
            .text(petoXML)
            .voiceId(VoiceId.IVY)
            .textType(TextType.SSML)
            .build(),
            FileSystems.getDefault().getPath(
                    "ekparolado",
                    "${SimpleDateFormat("yyyy-MM-dd HHmmss").format(Calendar.getInstance().time)}.ogg"
            )
    )

}

private fun pravigiIPA(vortoj: List<String>, ipa: List<String>): List<String> {
    val vortaro = Vortaro.alporti()
    return ipa.mapIndexed { index, s ->
        val elparolo = vortaro[vortoj[index]]?.elparolo
        elparolo ?: s
    }
}

@Serializable
data class Peto(val IPA: String)

@Serializable
data class Respondenhavo(
        val ĈuValida: Boolean = false,
        val Kialo: String = "",
        val IPA: List<String> = listOf()
)

private fun igiIPA(vortoj: String): List<String> {
    val lambda = LambdaClient.builder()
            .region(Region.US_EAST_1)
            .build()
    val respondo = lambda.invoke(InvokeRequest.builder()
            .functionName("Menteia-Kontrolilo")
            .payload(SdkBytes.fromUtf8String("{\"IPA\":\"$vortoj\"}"))
            .build()
    )
    val enhavo = JSON.parse(Respondenhavo.serializer(), respondo.payload().asUtf8String())
    if (enhavo.ĈuValida) {
        return enhavo.IPA
    } else {
        throw Exception(enhavo.Kialo)
    }
}

fun alportiListon(nomo: String): List<String> {
    val db = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build()
    val respondo = db.query(QueryRequest.builder()
            .tableName("Menteia-datumejo")
            .expressionAttributeValues(mapOf(":nomo" to AttributeValue.builder().s(nomo).build()))
            .keyConditionExpression("vorto = :nomo")
            .build()
    )
    if (respondo.count() != 1) {
        throw Exception("Ne eblis trovi la liston nomita ${nomo}")
    }
    return respondo.items()[0]["enhavo"]!!.l().map {
        it.s()
    }
}

data class Vorto(
        val vorto: String,
        val valenco: Int,
        val antaŭpaŭzo: Boolean,
        val interpaŭzo: Boolean,
        val elparolo: String?,
        val tipo: String?
)

object Vortaro {
    private val vortaro: MutableMap<String, Vorto> = mutableMapOf()

    fun alporti(): Map<String, Vorto> {
        if (vortaro.isNotEmpty()) {
            return vortaro
        }
        val db = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .build()
        val listo = db.scanPaginator(
                ScanRequest.builder().tableName("Menteia-datumejo")
                        .attributesToGet("vorto", "valenco", "antaŭpaŭzo", "interpaŭzo", "elparolo", "tipo")
                        .build()
        )
        listo.forEach {
            it.items().forEach {
                val vorto = it["vorto"]!!.s()
                val datumo = Vorto(
                        vorto = vorto,
                        valenco = it["valenco"]!!.n().toInt(),
                        antaŭpaŭzo = it["antaŭpaŭzo"]?.bool() ?: false,
                        interpaŭzo = it["interpaŭzo"]?.bool() ?: false,
                        elparolo = it["elparolo"]?.s(),
                        tipo = it["tipo"]?.s()
                )
                vortaro[vorto] = datumo
            }
        }
        return vortaro
    }
}