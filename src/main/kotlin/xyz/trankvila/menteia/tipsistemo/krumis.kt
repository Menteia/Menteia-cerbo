package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.tipsistemo.interna._certeco

class krumis(val _nomo: String): timis() {
    override val _tipo = _certeco.sagi

    override suspend fun _valuigi(): Any? {
        return this
    }

    suspend fun testos(): brodimis<timis> {
        val respondo = alirilaro.getWeatherStationState()
        val ĉefaparato = respondo.body.devices[0]
        val modulo1 = ĉefaparato.modules[0]
        val modulo2 = ĉefaparato.modules[1]

        val nevum = lemis.nombrigi(ĉefaparato.dashboard_data.Temperature.toBigDecimal(), 1)
        val dreta = lemis.ciferigi(ĉefaparato.dashboard_data.Humidity.toInt().toBigInteger())
        val co2 = lemis.ciferigi(ĉefaparato.dashboard_data.CO2.toInt().toBigInteger())
        val perom = lemis.ciferigi(ĉefaparato.dashboard_data.Pressure.toInt().toBigInteger())
        val balim = lemis.ciferigi(ĉefaparato.dashboard_data.Noise.toInt().toBigInteger())

        val (pluvo, temperaturo) = if (modulo1.type == "NAModule1") {
            Pair(modulo2, modulo1)
        } else {
            Pair(modulo1, modulo2)
        }

        val nevum2 = temperaturo.dashboard_data.Temperature!!
        val dreta2 = temperaturo.dashboard_data.Humidity!!
        val glimaSenam = pluvo.dashboard_data.Rain!!

        val valuoj = listOf(
                nevum(nevum),
                dreta(dreta),
                meforam(co2),
                perom(glima(perom)),
                balim(prena(balim)),
                nevum(lemis.nombrigi(nevum2.toBigDecimal(), 1)),
                dreta(lemis.ciferigi(dreta2.toInt().toBigInteger())),
                senam(lemis.nombrigi(glimaSenam.toBigDecimal(), 4))
        )

        return brotas.igiListon(valuoj)
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