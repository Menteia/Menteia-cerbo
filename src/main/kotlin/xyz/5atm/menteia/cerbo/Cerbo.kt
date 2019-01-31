package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.ʃanam.Tempo
import xyz.`5atm`.menteia.cerbo.lumina.Vetero
import xyz.`5atm`.menteia.cerbo.girisa.Listo
import java.lang.Exception

enum class Certeco(val vorto: String) {
    Negi("negi"), Sagi("sagi")
}

object Cerbo {
    suspend fun trakti(eniro: String): SintaksoArbo {
        val enirarbo = SintaksoArbo.konstrui(eniro)
        try {
            val respondo = when (enirarbo.radiko) {
                "doni" -> doni(enirarbo.opcioj[0])
                "keli" -> keli(enirarbo.opcioj[0])
                else -> "veguna"
            }
            return SintaksoArbo.konstrui(respondo)
        } catch (e: Exception) {
            e.printStackTrace()
            return SintaksoArbo("veguna", listOf())
        }
    }

    private suspend fun doni(eniro: SintaksoArbo): String {
        val (respondo, certeco) = when (eniro.radiko) {
            "ko" -> ko(eniro.opcioj[0])
            "kurimis" -> Listo.kurimis(eniro.opcioj[0], eniro.opcioj[1])
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
        return "${certeco.vorto} $respondo"
    }

    private suspend fun ko(eniro: SintaksoArbo): Pair<String, Certeco> {
        val (respondo, certeco) = when (eniro.radiko) {
            "geradas" -> Tempo.geradas()
            "fidinas" -> Tempo.fidinas()
            "lemona" -> Vetero.lemona(eniro.opcioj[0])
            "lurina" -> Vetero.lurina(eniro.opcioj[0], eniro.opcioj[1])
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