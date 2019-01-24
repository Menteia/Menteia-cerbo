package xyz.`5atm`.menteia

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.datumo.getThermostatAlt
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import kotlin.math.roundToInt

// Waterloo thermostat
object klisemi {
    suspend operator fun invoke(eco: SintaksoArbo): String {
        return when (eco.radiko) {
            "testos" -> testos()
            else -> throw Exception("Nekonita eco de klisemi: ${eco.radiko}")
        }
    }

    private suspend fun testos(): String {
        val raporto = getThermostatAlt("klisemi")
        return "sageta nevum ${Nombroj.nombrigi(raporto.ambient_temperature_c.roundToInt())} pomorom ${Nombroj.nombrigi(raporto.humidity.toInt())}"
    }
}