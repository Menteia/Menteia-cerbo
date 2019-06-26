import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import kotlinx.serialization.json.JsonUnknownKeyException
import kotlinx.serialization.parse
import org.dom4j.DocumentHelper
import org.dom4j.QName
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.core.async.AsyncResponseTransformer
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import software.amazon.awssdk.services.lambda.LambdaClient
import software.amazon.awssdk.services.lambda.model.InvokeRequest
import software.amazon.awssdk.services.polly.PollyAsyncClient
import software.amazon.awssdk.services.polly.PollyClient
import software.amazon.awssdk.services.polly.model.OutputFormat
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest
import software.amazon.awssdk.services.polly.model.TextType
import software.amazon.awssdk.services.polly.model.VoiceId
import xyz.trankvila.menteia.Agordo
import xyz.trankvila.menteia.tipsistemo.timis
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.vorttrakto.antaŭpaŭzoj
import xyz.trankvila.menteia.vorttrakto.elparolado
import xyz.trankvila.menteia.vorttrakto.interpaŭzoj
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.file.FileSystems
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ConcurrentHashMap

val polly = PollyAsyncClient.builder()
        .region(Region.US_WEST_2)
        .build()

data class Parolado(val id: UUID, val enhavo: ByteArray, val dato: LocalDate)
val paroladoj = ConcurrentHashMap<UUID, Deferred<Parolado>>()

internal fun paroli(arbo: timis): UUID {
    val petoXML = kreiXML(arbo)
    val respondo = polly.synthesizeSpeech(SynthesizeSpeechRequest.builder()
            .outputFormat(OutputFormat.OGG_VORBIS)
            .text(petoXML)
            .voiceId(VoiceId.IVY)
            .textType(TextType.SSML)
            .build(),
            AsyncResponseTransformer.toBytes()
    )
    val id = UUID.randomUUID()
    paroladoj[id] = GlobalScope.async {
        val rezulto = respondo.await().asByteArray()
        Parolado(id, rezulto, LocalDate.now())
    }
    return id
}

private fun kreiXML(frazo: timis): String {
    val xml = DocumentHelper.createDocument()
    val xmlRadiko = xml.addElement("speak")
            .addElement(QName("amazon:effect"))
            .addAttribute("vocal-tract-length", "+7%")
            .addElement("prosody")
            .addAttribute("rate", "70%")
            .addAttribute("volume", "+6dB")
    val vortoj = mutableListOf<String>()
    var spiro = 0
    frazo.traversi().forEach {
        if (it.startsWith('!')) {
            val ipa = igiIPA(vortoj.joinToString(" "))
            xmlRadiko.addElement("phoneme")
                    .addAttribute("alphabet", "ipa")
                    .addAttribute("ph", pravigiIPA(vortoj, ipa).joinToString(" "))
            when (it) {
                "!interpaŭzo" -> {
                    xmlRadiko.addElement("break").addAttribute("strength", "weak")
                }
                "!antaŭpaŭzo" -> {
                    xmlRadiko.addElement("break")
                }
            }
            vortoj.clear()
        } else {
            vortoj.add(it.replace('ŝ', 'ʃ'))
            ++spiro
        }
    }
    if (vortoj.isNotEmpty()) {
        val ipa = igiIPA(vortoj.joinToString(" "))
        xmlRadiko.addElement("phoneme")
                .addAttribute("alphabet", "ipa")
                .addAttribute("ph", pravigiIPA(vortoj, ipa).joinToString(" "))
    }
    return xml.asXML()
}

private fun pravigiIPA(vortoj: List<String>, ipa: List<String>): List<String> {
    return ipa.mapIndexed { index, s ->
        val elparolo = elparolado[vortoj[index]]
        elparolo ?: s
    }
}

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
    val respondteksto = respondo.payload().asUtf8String()
    val enhavo = JSON.parse(Respondenhavo.serializer(), respondteksto)
    return if (enhavo.ĈuValida) {
        enhavo.IPA
    } else {
        throw Exception(enhavo.Kialo)
    }
}

data class Vorto(
        val vorto: String,
        val tipo: String
)

object Vortaro {
    private val vortaro: MutableMap<String, Vorto> = mutableMapOf()

    fun alporti(alporti: Boolean = false): Map<String, Vorto> {
        if (vortaro.isNotEmpty() && !alporti) {
            return vortaro
        }
        vortaro.clear()
        val db = DynamoDbClient.builder()
                .region(Region.US_WEST_2)
                .build()
        val listo = db.scanPaginator(
                ScanRequest.builder().tableName("Menteia-datumejo")
                        .attributesToGet(
                                "vorto",
                                "tipo"
                        )
                        .build()
        )
        listo.forEach {
            it.items().forEach {
                val vorto = it["vorto"]!!.s()
                val datumo = Vorto(
                        vorto = vorto,
                        tipo = it["tipo"]!!.s()
                )
                vortaro[vorto] = datumo
            }
        }
        return vortaro
    }
}