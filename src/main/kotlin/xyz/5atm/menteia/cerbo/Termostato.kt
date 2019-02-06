package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import xyz.`5atm`.menteia.datumo.*
import kotlin.math.roundToInt

internal interface Termostato : NomitaAĵo {
    override suspend operator fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "testos" -> testos()
            "gremina" -> gremina()
            else -> super.invoke(eco)
        }
    }

    override suspend operator fun invoke(eco: SintaksoArbo, valuo: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "sevara" -> {
                alirilaro.setThermostatTemperature(nomo, Iloj.legiTemperaturon(valuo))
                "to des $nomo $eco $valuo" to Certeco.Regi
            }
            "gremina" -> {
                when (valuo.radiko) {
                    "saresa", "silega" -> alirilaro.setThermostatMode(nomo, valuo.radiko, Iloj.legiTemperaturon(valuo.opcioj[0]))
                    "sasigas" -> alirilaro.setThermostatMode(nomo, valuo.radiko,
                            Iloj.legiTemperaturon(valuo.opcioj[0]), Iloj.legiTemperaturon(valuo.opcioj[1]))
                    "maraga" -> alirilaro.setThermostatMode(nomo, valuo.radiko)
                    else -> throw Exception("Nekonita modo: ${valuo.radiko}")
                }
                "to des $nomo $eco $valuo" to Certeco.Regi
            }
            else -> super.invoke(eco, valuo)
        }
    }

    suspend fun testos(): Pair<String, Certeco> {
        val raporto = alirilaro.getThermostatState(nomo)
        return when (raporto.hvac_state) {
            "heating" -> "saresa nevum ${Nombroj.nombrigi(raporto.target_temperature_c!!.toInt())}"
            "cooling" -> "silega nevum ${Nombroj.nombrigi(raporto.target_temperature_c!!.toInt())}"
            "off" -> "buve"
            else -> throw Exception("Ne konita ŝtato de $nomo: ${raporto.hvac_state}")
        } to Certeco.Sagi
    }

    suspend fun gremina(): Pair<String, Certeco> {
        val raporto = alirilaro.getThermostatState(nomo)
        val modo = hvacModes.getValue(raporto.hvac_mode)
        val temperaturo = when (raporto.hvac_mode) {
            "heat", "cool" -> " nevum ${Nombroj.nombrigi(raporto.target_temperature_c!!.roundToInt())}"
            "heat-cool" -> " nevum ${Nombroj.nombrigi(raporto.target_temperature_low_c!!.roundToInt())} nevum ${Nombroj.nombrigi(raporto.target_temperature_high_c!!.roundToInt())}"
            else -> ""
        }
        return modo + temperaturo to Certeco.Sagi
    }
}

// Waterloo thermostat
internal object klisemi : Termostato {
    override val nomo = "klisemi"
}

internal object brinemi : Termostato {
    override val nomo = "brinemi"
}