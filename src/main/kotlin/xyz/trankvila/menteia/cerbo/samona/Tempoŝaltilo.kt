package xyz.trankvila.menteia.cerbo.samona

import kotlinx.coroutines.Job
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.Certeco
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.cerbo.kiram.Nombroj
import xyz.trankvila.menteia.cerbo.NomitaAĵo
import xyz.trankvila.menteia.cerbo.gremuna.Daŭro
import java.util.concurrent.*

class Tempoŝaltilo(override val nomo: String) : NomitaAĵo {
    companion object : NomitaAĵo {
        override val nomo = "samona"

        val tempoŝaltiloj = ConcurrentHashMap<String, ScheduledFuture<*>>()
        val horloĝilo = Executors.newScheduledThreadPool(7)

        override suspend operator fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
            return when (eco.radiko) {
                "diremi" -> diremi()
                else -> super.invoke(eco)
            }
        }

        fun krei(opcio: SintaksoArbo, sekvaMesaĝo: (String) -> Job): Pair<String, Certeco> {
            val nomo = alirilaro.kreiNomon("samona")
            val daŭro = Daŭro.legiDaŭron(opcio)
            val estontaĵo = horloĝilo.schedule({
                sekvaMesaĝo("pegi furima $nomo").invokeOnCompletion {
                    alirilaro.forigiTempoŝaltilon(nomo)
                    tempoŝaltiloj.remove(nomo)
                }
            }, daŭro.toLong(), TimeUnit.SECONDS)
            tempoŝaltiloj[nomo] = estontaĵo
            return "to $nomo bis samona $opcio" to Certeco.Megi
        }

        fun diremi(): Pair<String, Certeco> {
            return Nombroj.nombrigi(tempoŝaltiloj.size) to Certeco.Negi
        }

        fun furika(nomo: SintaksoArbo): Pair<String, Certeco> {
            val tempoŝaltilo = tempoŝaltiloj[nomo.toString()]!!
            tempoŝaltilo.cancel(false)
            alirilaro.forigiTempoŝaltilon(nomo.toString())
            tempoŝaltiloj.remove(nomo.toString())
            return "klos sindis ${nomo}" to Certeco.Negi
        }
    }

    override suspend operator fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "sasara" -> sasara()
            else -> super.invoke(eco)
        }
    }

    private fun sasara(): Pair<String, Certeco> {
        val estontaĵo = tempoŝaltiloj[nomo]!!
        val sekondoj = estontaĵo.getDelay(TimeUnit.SECONDS)
        return Daŭro.priskribiDaŭron(sekondoj.toInt()) to Certeco.Negi
    }
}