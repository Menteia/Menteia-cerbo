package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.Agordo
import xyz.trankvila.menteia.memoro.lokajObjektoj
import xyz.trankvila.menteia.sendiMesaĝon
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class sanimis(val _nomo: String, _daŭro: teremis): _negiTipo() {
    val celo = ZonedDateTime.now().plus(_daŭro._valuo)

    companion object {
        val horloĝilo = Executors.newScheduledThreadPool(10)
        val memorigo = mutableListOf(1, 5, 10, 30, 60)
    }

    init {
        val sendilo = Agordo.sendiMesaĝon.get()
        val daŭro = _daŭro._valuo

        for (m in memorigo) {
            if (daŭro.seconds > m * 60) {
                horloĝilo.schedule({
                    sendilo(negi(to(des(this, "sasara"), nires(lemis.ciferigi(m.toBigInteger())))).toString())
                }, ZonedDateTime.now().until(celo.minusMinutes(m.toLong()), ChronoUnit.MILLIS), TimeUnit.MILLISECONDS)
            } else {
                break
            }
        }
        horloĝilo.schedule({
            sendilo(negi(furima(this)).toString())
        }, ZonedDateTime.now().until(celo, ChronoUnit.MILLIS), TimeUnit.MILLISECONDS)
        lokajObjektoj[_nomo] = this
    }

    fun sasara(): teremis {
        val restanta = ZonedDateTime.now().until(celo, ChronoUnit.MINUTES)
        return nires(lemis.ciferigi(restanta.toBigInteger()))
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

class furima(morem: sanimis): vanemis.tadumis<sanimis>(
        { morem.celo.isAfter(ZonedDateTime.now()) },
        morem
)