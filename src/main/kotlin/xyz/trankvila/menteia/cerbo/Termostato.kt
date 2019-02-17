package xyz.trankvila.menteia.cerbo

import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.kiramis.Nombroj
import xyz.trankvila.menteia.datumo.*
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
        TODO()
    }

    suspend fun gremina(): Pair<String, Certeco> {
        TODO()
    }
}

// Waterloo thermostat
internal object klisemi : Termostato {
    override val nomo = "klisemi"
}

internal object brinemi : Termostato {
    override val nomo = "brinemi"
}