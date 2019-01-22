import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import kotlinx.serialization.parse
import org.dom4j.DocumentHelper
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
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
            .addElement("amazon:effect")
            .addAttribute("vocal-tract-length", "+7%")
    val vortoj = mutableListOf<String>()
    arbo.traversi(kunPaŭzoj = true).forEach {
        if (it.startsWith("!")) {
            val ipa = igiIPA(vortoj.joinToString(" "))
            xmlRadiko.addElement("phoneme")
                    .addAttribute("alphabet", "ipa")
                    .addAttribute("ph", ipa.joinToString(" "))
            when (it) {
                "!longapaŭzo" -> xmlRadiko.addElement("break")
                "!paŭzo" -> xmlRadiko.addElement("break").addAttribute("strength", "weak")
            }
            vortoj.clear()
        }
    }
    val petoXML = xml.asXML()
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

@Serializable
data class Peto(val IPA: String)

@Serializable
data class Respondenhavo(
        val ĈuFinita: Boolean = false,
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
    if (enhavo.ĈuFinita) {
        return enhavo.IPA
    } else {
        throw Exception(enhavo.Kialo)
    }
}

data class Vorto(val vorto: String, val valenco: Int, val antaŭpaŭzo: Boolean, val interpaŭzo: Boolean)

object Vortaro {
    private val vortaro: MutableMap<String, Vorto> = mutableMapOf()

    fun alporti(): Map<String, Vorto> {
        if (vortaro.isNotEmpty()) {
            return vortaro
        }
        val db = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .build()
        val respondo = db.scan(ScanRequest.builder().tableName("Menteia-vortaro").build())
        respondo.items().forEach {
            val vorto = it["vorto"]!!.s()
            val datumo = Vorto(
                    vorto = vorto,
                    valenco = it["valenco"]!!.n().toInt(),
                    antaŭpaŭzo = it["antaŭpaŭzo"]?.bool() ?: false,
                    interpaŭzo = it["interpaŭzo"]?.bool() ?: false
            )
            vortaro[vorto] = datumo
        }
        return vortaro
    }
}