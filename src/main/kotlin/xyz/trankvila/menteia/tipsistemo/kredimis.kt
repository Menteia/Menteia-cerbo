package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.datumo.alirilaro
import java.lang.Exception

interface kredrimis : renas

interface kresimis : renas

class kredimis(val nomo: String): _sagiTipo(), nadimis {
    override suspend fun _valuigi(): Any? {
        return this
    }

    suspend fun testos(): kredrimis {
        val (key, id) = alirilaro.alportiTermostaton(nomo)
        val ŝtato = alirilaro.getThermostatState(id, key)
        return when (ŝtato.hvac_state) {
            "heating" -> saresa(nevum(lemis.nombrigi(ŝtato.target_temperature_c!!.toBigDecimal(), 1)))
            "cooling" -> silega(nevum(lemis.nombrigi(ŝtato.target_temperature_c!!.toBigDecimal(), 1)))
            "off" -> buve()
            else -> throw Exception("Nekonita ŝtato: ${ŝtato.hvac_state}")
        }
    }

    suspend fun gremina(): kresimis {
        val (key, id) = alirilaro.alportiTermostaton(nomo)
        val ŝtato = alirilaro.getThermostatState(id, key)
        return when (ŝtato.hvac_mode) {
            "heat" -> saresa(nevum(lemis.nombrigi(ŝtato.target_temperature_c!!.toBigDecimal(), 1)))
            "cool" -> silega(nevum(lemis.nombrigi(ŝtato.target_temperature_c!!.toBigDecimal(), 1)))
            "heat-cool" -> {
                val temp1 = lemis.nombrigi(ŝtato.target_temperature_low_c!!.toBigDecimal(), 1)
                val temp2 = lemis.nombrigi(ŝtato.target_temperature_high_c!!.toBigDecimal(), 1)
                sasigas(nevum(temp1), nevum(temp2))
            }
            "eco", "off" -> maraga()
            else -> throw Exception("Nekonita modot: ${ŝtato.hvac_mode}")
        }
    }

    override fun toString(): String {
        return nomo
    }

    override fun traversi(): Sequence<String> {
        return sequence {
            yield(nomo)
        }
    }
}

class saresa(val temperaturo: `nomis nevum`): _modo(temperaturo), kredrimis, kresimis
class silega(val temperaturo: `nomis nevum`): _modo(temperaturo), kredrimis, kresimis
class sasigas(val temperaturo1: `nomis nevum`, val temperaturo2: `nomis nevum`): _modo(temperaturo1, temperaturo2), kresimis
class maraga: _modo(), kresimis