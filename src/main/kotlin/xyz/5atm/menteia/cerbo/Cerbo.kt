package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.ʃanam.Tempo
import xyz.`5atm`.menteia.cerbo.lumina.Vetero
import java.lang.Exception

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
            print(e)
            return SintaksoArbo("valono", listOf())
        }
    }

    fun doni(eniro: SintaksoArbo): String {
        val respondo = when (eniro.radiko) {
            "ko" -> ko(eniro.opcioj[0])
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
        return "sagi $respondo"
    }

    suspend fun keli(eniro: SintaksoArbo): String {
        return when (eniro.radiko) {
            "lemona" -> "sagi to ${eniro} ${Vetero.lemona(eniro.opcioj[0])}"
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
    }

    fun ko(eniro: SintaksoArbo): String {
        val respondo = when (eniro.radiko) {
            "geradas" -> Tempo.geradas()
            "fidinas" -> Tempo.fidinas()
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
        return "to ${eniro} ${respondo}"
    }
}