package xyz.`5atm`.menteia

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.datumo.getThermostatAlt
import xyz.`5atm`.menteia.datumo.getState
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import xyz.`5atm`.menteia.cerbo.Iloj
import xyz.`5atm`.menteia.datumo.hvacModes
import kotlin.math.roundToInt

interface Objekto {
    suspend operator fun invoke(eco: SintaksoArbo): String
}

val objektoj = mapOf(
        "klisemi" to klisemi,
        "frodeni" to frodeni
)

// Waterloo thermostat
private object klisemi : Objekto {
    override suspend operator fun invoke(eco: SintaksoArbo): String {
        return when (eco.radiko) {
            "testos" -> testos()
            else -> throw Exception("Nekonita eco de klisemi: ${eco.radiko}")
        }
    }

    private suspend fun testos(): String {
        val raporto = getThermostatAlt("klisemi")
        return "sageta nevum ${Nombroj.nombrigi(raporto.ambient_temperature_c.roundToInt())} pomorom ${Nombroj.nombrigi(raporto.humidity.toInt())} ${hvacModes[raporto.hvac_mode]!!}"
    }
}

private object frodeni : Objekto {
    override suspend operator fun invoke(eco: SintaksoArbo): String {
        return when (eco.radiko) {
            "testos" -> testos()
            "taskesos" -> taskesos()
            else -> throw Exception("Nekonita eco de frodeni: ${eco.radiko}")
        }
    }

    private suspend fun testos(): String {
        val raporto = getState()
        val ĉefdatumo = raporto.body.devices[0].dashboard_data
        val nevum = Nombroj.nombrigi(ĉefdatumo.Temperature.toInt())
        val pomorom = Nombroj.nombrigi(ĉefdatumo.Humidity.toInt())
        val co2 = Nombroj.nombrigi(ĉefdatumo.CO2.toInt())
        return "sadika nevum ${nevum} pomorom ${pomorom} meforam ${co2}"
    }

    private suspend fun taskesos(): String {
        val raporto = getState()
        val ĉefaparato = raporto.body.devices[0]
        val modulo1 = ĉefaparato.modules[0]
        val modulo2 = ĉefaparato.modules[1]

        val nevum = Nombroj.nombrigi(ĉefaparato.dashboard_data.Temperature.toInt())
        val pomorom = Nombroj.nombrigi(ĉefaparato.dashboard_data.Humidity.toInt())
        val co2 = Nombroj.nombrigi(ĉefaparato.dashboard_data.CO2.toInt())
        val perom = Nombroj.nombrigi(ĉefaparato.dashboard_data.Pressure.toInt())
        val balim = Nombroj.nombrigi(ĉefaparato.dashboard_data.Noise.toInt())
        val listo = mutableListOf(
                "nevum $nevum",
                "pomorom $pomorom",
                "meforam $co2",
                "glima perom $perom",
                "prena balim $balim"
        )

        val (pluvo, temperaturo) = if (modulo1.type == "NAModule1") {
            Pair(modulo2, modulo1)
        } else {
            Pair(modulo1, modulo2)
        }

        val nevum2 = temperaturo.dashboard_data.Temperature!!
        val pomorom2 = temperaturo.dashboard_data.Humidity!!

        listo.add("nevum ${Nombroj.nombrigi(nevum2.toInt())}")
        listo.add("pomorom ${Nombroj.nombrigi(pomorom2.toInt())}")

        val glimaSenam = pluvo.dashboard_data.Rain!!

        listo.add("glima senam ${Nombroj.nombrigi(glimaSenam.toInt())}")

        return Iloj.listigi(listo)
    }
}