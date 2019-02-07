package xyz.trankvila.menteia.cerbo

import kotlinx.coroutines.Job
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.ʃanam.Tempo
import xyz.trankvila.menteia.cerbo.lumina.Vetero
import xyz.trankvila.menteia.cerbo.girisa.Listo
import java.lang.Exception

enum class Certeco(val vorto: String) {
    Negi("negi"), Sagi("sagi"), Megi("megi"), Regi("regi"), Pegi("pegi")
}

object Cerbo {
    suspend fun trakti(eniro: String, sekvaMesaĝo: (String) -> Job): SintaksoArbo {
        try {
            val enirarbo = SintaksoArbo.konstrui(eniro)
            val respondo = when (enirarbo.radiko) {
                "doni" -> doni(enirarbo.opcioj[0])
                "keli" -> keli(enirarbo.opcioj[0], sekvaMesaĝo)
                else -> "veguna"
            }
            return SintaksoArbo.konstrui(respondo)
        } catch (e: MenteiaEksepcio) {
            return SintaksoArbo.konstrui("pegi ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    internal suspend fun doni(eniro: SintaksoArbo): String {
        val (respondo, certeco) = when (eniro.radiko) {
            "ko" -> ko(eniro.opcioj[0])
            "kurimis" -> Listo.kurimis(eniro.opcioj[0], eniro.opcioj[1])
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
        return "${certeco.vorto} $respondo"
    }

    internal suspend fun ko(eniro: SintaksoArbo): Pair<String, Certeco> {
        val (respondo, certeco) = when (eniro.radiko) {
            "geradas" -> Tempo.geradas()
            "fidinas" -> Tempo.fidinas()
            "lemona" -> Vetero.lemona(eniro.opcioj[0])
            "lurina" -> Vetero.lurina(eniro.opcioj[0], eniro.opcioj[1])
            "tremos" -> Listo.tremos(eniro.opcioj[0])
            "des" -> koDes(eniro.opcioj[0], eniro.opcioj[1])
            else -> {
                val aĵo = troviNomitanAĵon(eniro)
                aĵo.priskribi()
            }
        }
        return "to ${eniro} ${respondo}" to certeco
    }

    private suspend fun koDes(objektoNomo: SintaksoArbo, eco: SintaksoArbo): Pair<String, Certeco> {
        val objekto = troviNomitanAĵon(objektoNomo)
        return objekto(eco)
    }
}