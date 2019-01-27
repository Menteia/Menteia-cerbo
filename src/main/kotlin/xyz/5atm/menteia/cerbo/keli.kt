package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.objektoj
import java.lang.Exception

object keli {
    suspend operator fun invoke(eniro: SintaksoArbo): String {
        return when (eniro.radiko) {
            "miris" -> {
                val rezulto = miris(eniro.opcioj[0], eniro.opcioj[1])
                "sagi $rezulto"
            }
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
    }

    private suspend fun miris(celo: SintaksoArbo, valuo: SintaksoArbo): String {
        return when (celo.radiko) {
            "des" -> {
                val objekto = objektoj[celo.opcioj[0].radiko]
                        ?: throw Exception("Nekonita objekto ${celo.opcioj[0]}")
                objekto(celo.opcioj[1], valuo)
            }
            else -> throw Exception("Ne komprenis ${celo.radiko}")
        }
    }
}