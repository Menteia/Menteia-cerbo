package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.ʃanam.Tempo
import xyz.`5atm`.menteia.cerbo.lumina.Vetero
import xyz.`5atm`.menteia.objektoj
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
            e.printStackTrace()
            return SintaksoArbo("veguna", listOf())
        }
    }

    suspend fun doni(eniro: SintaksoArbo): String {
        val respondo = when (eniro.radiko) {
            "ko" -> ko(eniro.opcioj[0])
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
        return "sagi $respondo"
    }

    fun keli(eniro: SintaksoArbo): String {
        throw Exception("Ne komprenis ${eniro.radiko}")
    }

    suspend fun ko(eniro: SintaksoArbo): String {
        val respondo = when (eniro.radiko) {
            "geradas" -> Tempo.geradas()
            "fidinas" -> Tempo.fidinas()
            "lemona" -> Vetero.lemona(eniro.opcioj[0])
            "lurina" -> Vetero.lurina(eniro.opcioj[0], eniro.opcioj[1])
            "des" -> koDes(eniro.opcioj[0], eniro.opcioj[1])
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
        return "to ${eniro} ${respondo}"
    }

    suspend fun koDes(objektoNomo: SintaksoArbo, eco: SintaksoArbo): String {
        val objekto = objektoj[objektoNomo.radiko] ?: throw Exception("Ne konita objekto: ${objektoNomo.radiko}")
        return objekto(eco)
    }
}