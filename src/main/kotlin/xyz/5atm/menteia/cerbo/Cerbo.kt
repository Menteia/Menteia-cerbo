package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.ʃanam.Tempo
import java.lang.Exception

object Cerbo {
    fun trakti(eniro: String): SintaksoArbo {
        val enirarbo = SintaksoArbo.konstrui(eniro)
        try {
            val respondo = when (enirarbo.radiko) {
                "doni" -> doni(enirarbo.opcioj[0])
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

    fun ko(eniro: SintaksoArbo): String {
        val respondo = when (eniro.radiko) {
            "geradas" -> Tempo.geradas()
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
        return "to ${eniro} ${respondo}"
    }
}