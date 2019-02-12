package xyz.trankvila.menteia.cerbo

import kotlinx.coroutines.Job
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.brodimis.Listo
import xyz.trankvila.menteia.cerbo.sanimis.Tempoŝaltilo
import xyz.trankvila.menteia.cerbo.timis.Tipsistemo
import java.lang.Exception

object keli {
    suspend operator fun invoke(eniro: SintaksoArbo, sekvaMesaĝo: (String) -> Job): String {
        val (respondo, certeco) = when (eniro.radiko) {
            "furika" -> furika(eniro.opcioj[0])
            "miris" ->  miris(eniro.opcioj[0], eniro.opcioj[1])
            "karema" -> Listo.karema(eniro.opcioj[0], eniro.opcioj[1], eniro.opcioj[2])
            "kirema" -> Listo.kirema(eniro.opcioj[0], eniro.opcioj[1])
            "karisi" -> Listo.karisi(eniro.opcioj[0], eniro.opcioj[1])
            "marina" -> marina(eniro.opcioj[0], sekvaMesaĝo)
            "vidina" -> Listo.vidina(eniro.opcioj[0], sekvaMesaĝo)
            "buvi" -> Lumo.buvi(eniro.opcioj[0])
            "mavi" -> Lumo.mavi(eniro.opcioj[0])
            else -> throw Exception("Ne komprenis ${eniro.radiko}")
        }
        return "${certeco.vorto} $respondo"
    }

    private suspend fun miris(celo: SintaksoArbo, valuo: SintaksoArbo): Pair<String, Certeco> {
        return when (celo.radiko) {
            "des" -> {
                val objekto = objektoj[celo.opcioj[0].radiko]
                        ?: throw Exception("Nekonita objekto ${celo.opcioj[0]}")
                objekto(celo.opcioj[1], valuo)
            }
            else -> throw Exception("Ne komprenis ${celo.radiko}")
        }
    }

    private fun marina(tipo: SintaksoArbo, sekvaMesaĝo: (String) -> Job): Pair<String, Certeco> {
        return when (tipo.radiko) {
            "brodimis" -> Listo.marina(tipo.opcioj[0])
            "bis" -> marinaBis(tipo.opcioj[0], tipo.opcioj[1], sekvaMesaĝo)
            else -> throw Exception("Ne eblas krei tipon $tipo")
        }
    }

    private fun marinaBis(tipo: SintaksoArbo, opcio: SintaksoArbo, sekvaMesaĝo: (String) -> Job): Pair<String, Certeco> {
        return when (tipo.radiko) {
            "samona" -> Tempoŝaltilo.krei(opcio, sekvaMesaĝo)
            else -> throw Exception("Ne eblas krei tipon $tipo per $opcio")
        }
    }

    private fun furika(nomo: SintaksoArbo): Pair<String, Certeco> {
        val vorto = Vortaro.alporti()[nomo.radiko] ?: throw MenteiaEksepcio("klos tinas $nomo")
        val tipo = Tipsistemo.legiTipon(vorto.tipo!!)
        return when (tipo[0]) {
            "brodimis" -> Listo.furika(nomo)
            "samona" -> Tempoŝaltilo.furika(nomo)
            else -> throw Exception("Ne eblas forigi $nomo de tipo ${vorto.tipo}")
        }
    }
}