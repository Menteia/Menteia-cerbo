package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.girisa.Listo
import java.lang.Exception

object keli {
    suspend operator fun invoke(eniro: SintaksoArbo): String {
        val (respondo, certeco) = when (eniro.radiko) {
            "furika" -> furika(eniro.opcioj[0])
            "miris" ->  miris(eniro.opcioj[0], eniro.opcioj[1])
            "karema" -> Listo.karema(eniro.opcioj[0], eniro.opcioj[1], eniro.opcioj[2])
            "kirema" -> Listo.kirema(eniro.opcioj[0], eniro.opcioj[1])
            "karisi" -> Listo.karisi(eniro.opcioj[0], eniro.opcioj[1])
            "marina" -> marina(eniro.opcioj[0])
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
        return "${certeco.vorto} $respondo"
    }

    private suspend fun miris(celo: SintaksoArbo, valuo: SintaksoArbo): Pair<String, Certeco> {
        return when (celo.radiko) {
            "des" -> {
                val objekto = objektoj[celo.opcioj[0].radiko]
                        ?: throw Exception("Nekonita objekto ${celo.opcioj[0]}")
                objekto(celo.opcioj[1], valuo) to Certeco.Sagi
            }
            else -> throw Exception("Ne komprenis ${celo.radiko}")
        }
    }

    private fun marina(tipo: SintaksoArbo): Pair<String, Certeco> {
        return when (tipo.radiko) {
            "girisa" -> Listo.marina()
            else -> throw Exception("Ne eblas krei tipon $tipo")
        }
    }

    private fun furika(nomo: SintaksoArbo): Pair<String, Certeco> {
        val vorto = Vortaro.alporti()[nomo.radiko] ?: throw MenteiaEksepcio("klos tinas $nomo")
        return when (vorto.tipo) {
            "girisa" -> Listo.furika(nomo)
            else -> throw Exception("Ne eblas forigi $nomo de tipo ${vorto.tipo}")
        }
    }
}