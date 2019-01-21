import org.dom4j.DocumentHelper
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.polly.PollyClient
import software.amazon.awssdk.services.polly.PollyClientBuilder
import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo

internal fun paroli(arbo: SintaksoArbo): Unit {
    val polly = PollyClient.builder()
            .region(Region.US_WEST_2)
            .build()
    val xml = DocumentHelper.createDocument()
    val xmlRadiko = xml.addElement("speak")
            .addElement("amazon:effect")
            .addAttribute("vocal-tract-length", "+7%")
    var vortoj = mutableListOf<String>()
    arbo.traversi(kunPaŭzoj = true).forEach {
        if (it.startsWith("!")) {

        }
    }
}

private fun igiIPA(vortoj: Array<String>): List<String> {
    val IPA = mutableListOf<String>()
    vortoj.forEach {
        when (it) {
            "sagi" -> IPA.add("sə'gi")
            else -> {

            }
        }
    }
    return IPA
}