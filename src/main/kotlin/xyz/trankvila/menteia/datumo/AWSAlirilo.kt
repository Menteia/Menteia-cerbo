import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import kotlinx.serialization.parse
import org.dom4j.DocumentHelper
import org.dom4j.QName
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import software.amazon.awssdk.services.lambda.LambdaClient
import software.amazon.awssdk.services.lambda.model.InvokeRequest
import software.amazon.awssdk.services.polly.PollyClient
import software.amazon.awssdk.services.polly.model.OutputFormat
import software.amazon.awssdk.services.polly.model.SynthesizeSpeechRequest
import software.amazon.awssdk.services.polly.model.TextType
import software.amazon.awssdk.services.polly.model.VoiceId
import xyz.trankvila.menteia.tipsistemo.timis
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.vorttrakto.antaŭpaŭzoj
import xyz.trankvila.menteia.vorttrakto.elparolado
import xyz.trankvila.menteia.vorttrakto.interpaŭzoj
import java.lang.Exception
import java.nio.file.FileSystems
import java.text.SimpleDateFormat
import java.util.*

val polly = PollyClient.builder()
        .region(Region.US_WEST_2)
        .build()

internal fun paroli(arbo: timis): ByteArray {
    val petoXML = kreiXML(arbo)
    val respondo = polly.synthesizeSpeech(SynthesizeSpeechRequest.builder()
            .outputFormat(OutputFormat.OGG_VORBIS)
            .text(petoXML)
            .voiceId(VoiceId.IVY)
            .textType(TextType.SSML)
            .build()
    )
    return respondo.readBytes()
}

private val mallongaspiro = 14
private val longaspiro = 19

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
                    if (spiro > mallongaspiro) {
                        spiro = 0
                        xmlRadiko.addElement(QName("amazon:breath"))
                    } else {
                        xmlRadiko.addElement("break").addAttribute("strength", "weak")
                    }
                }
                "!antaŭpaŭzo" -> {
                    if (spiro > mallongaspiro) {
                        spiro = 0
                        xmlRadiko.addElement(QName("amazon:breath"))
                    } else if (spiro > longaspiro) {
                        spiro = 0
                        xmlRadiko.addElement(QName("amazon:breath"))
                                .addAttribute("volume", "x-loud")
                                .addAttribute("duration", "long")
                    } else {
                        xmlRadiko.addElement("break")
                    }
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
    val enhavo = JSON.parse(Respondenhavo.serializer(), respondo.payload().asUtf8String())
    if (enhavo.ĈuValida) {
        return enhavo.IPA
    } else {
        println(vortoj)
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