package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import xyz.trankvila.menteia.Agordo
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.memoro.lokajObjektoj
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

interface _forigebla: renas
interface _kreebla: renas

class sanimis(val _nomo: String, _daŭro: teremis): timis(), _forigebla, _kreebla {
    val celo = runBlocking {
        ZonedDateTime.now().plus(_daŭro._valuigi()).withNano(0)
    }
    val _sciigoj = mutableListOf<ScheduledFuture<*>>()
    override val _tipo = _certeco.negi

    companion object {
        val horloĝilo = Executors.newScheduledThreadPool(10)
        val memorigo = mutableListOf(1, 5, 10, 30, 60)
    }

    init {
        val sendilo = Agordo.sendiMesaĝon.get()
        val daŭro = runBlocking { _daŭro._valuigi() }

        for (m in memorigo) {
            if (daŭro.seconds > m * 60) {
                _sciigoj.add(horloĝilo.schedule({
                    sendilo(negi(to(des(this, _nomitaAĵo("sasara")), nires(lemis.ciferigi(m.toBigInteger())))))
                }, ZonedDateTime.now().until(celo.minusMinutes(m.toLong()), ChronoUnit.MILLIS), TimeUnit.MILLISECONDS))
            } else {
                break
            }
        }
        _sciigoj.add(horloĝilo.schedule({
            runBlocking {
                alirilaro.forigiTempoŝaltilon(_nomo)
                lokajObjektoj.remove(_nomo)
                sendilo(pegi(klos(sindis(this@sanimis))))
            }
        }, ZonedDateTime.now().until(celo, ChronoUnit.MILLIS), TimeUnit.MILLISECONDS))
        lokajObjektoj[_nomo] = this
    }

    fun sasara(): teremis {
        val totalajSekondoj = ZonedDateTime.now().until(celo, ChronoUnit.SECONDS)
        val horoj = totalajSekondoj / 3600
        val minutoj = totalajSekondoj % 3600 / 60
        val sekondoj = totalajSekondoj % 60
        return when {
            horoj > 0 -> gomos(lemis.ciferigi(horoj.toBigInteger()))
            minutoj > 0 -> nires(lemis.ciferigi(minutoj.toBigInteger()))
            else -> trinis(lemis.ciferigi(sekondoj.toBigInteger()))
        }
    }

    fun _forigiSciigojn() {
        _sciigoj.forEach {
            it.cancel(false)
        }
    }

    override suspend fun _valuigi(): Any? {
        return this
    }

    override fun toString(): String {
        return _nomo
    }

    override fun traversi(): Sequence<String> {
        return sequence {
            yield(_nomo)
        }
    }
}

@Serializable
internal data class _tempoŝaltilo(val _nomo: String, val _fino: String)