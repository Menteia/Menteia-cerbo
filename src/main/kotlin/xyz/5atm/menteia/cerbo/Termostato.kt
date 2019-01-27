package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.datumo.getThermostatAlt
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import xyz.`5atm`.menteia.datumo.setThermostat

internal interface Termostato : Objekto {
    override suspend operator fun invoke(eco: SintaksoArbo): String {
        return when (eco.radiko) {
            "testos" -> testos()
            else -> super.invoke(eco)
        }
    }

    override suspend operator fun invoke(eco: SintaksoArbo, valuo: SintaksoArbo): String {
        return when (eco.radiko) {
            "sevara" -> {
                when (valuo.radiko) {
                    "nevum" -> setThermostat(nomo, Nombroj.legiNombron(valuo.opcioj[0]))
                    else -> throw Exception("Nekonita vorto: ${valuo.radiko}")
                }
                "miras des $nomo $eco $valuo"
            }
            else -> super.invoke(eco, valuo)
        }
    }

    suspend fun testos(): String {
        val raporto = getThermostatAlt(nomo)
        return when (raporto.hvac_state) {
            "heating" -> "saresa nevum ${Nombroj.nombrigi(raporto.target_temperature_c!!.toInt())}"
            "cooling" -> "silega nevum ${Nombroj.nombrigi(raporto.target_temperature_c!!.toInt())}"
            "off" -> "buve"
            else -> throw Exception("Ne konita ŝtato de $nomo: ${raporto.hvac_state}")
        }
    }
}

// Waterloo thermostat
internal object klisemi : Termostato {
    override val nomo = "klisemi"
}

internal object brinemi : Termostato {
    override val nomo = "brinemi"
}