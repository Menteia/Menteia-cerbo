package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import xyz.`5atm`.menteia.datumo.*

internal object frodeni : NomitaAĵo {
    override val nomo = "frodeni"

    override suspend operator fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "testos" -> testos()
            "taskesos" -> taskesos()
            else -> super.invoke(eco)
        }
    }

    private suspend fun testos(): Pair<String, Certeco> {
        val raporto = alirilaro.getWeatherStationState()
        val ĉefdatumo = raporto.body.devices[0].dashboard_data
        val nevum = Nombroj.nombrigi(ĉefdatumo.Temperature.toInt())
        val dreta = Nombroj.nombrigi(ĉefdatumo.Humidity.toInt())
        val co2 = Nombroj.nombrigi(ĉefdatumo.CO2.toInt())
        return "sadika nevum ${nevum} dreta ${dreta} meforam ${co2}" to Certeco.Sagi
    }

    private suspend fun taskesos(): Pair<String, Certeco> {
        val raporto = alirilaro.getWeatherStationState()
        val ĉefaparato = raporto.body.devices[0]
        val modulo1 = ĉefaparato.modules[0]
        val modulo2 = ĉefaparato.modules[1]

        val nevum = Nombroj.nombrigi(ĉefaparato.dashboard_data.Temperature.toInt())
        val dreta = Nombroj.nombrigi(ĉefaparato.dashboard_data.Humidity.toInt())
        val co2 = Nombroj.nombrigi(ĉefaparato.dashboard_data.CO2.toInt())
        val perom = Nombroj.nombrigi(ĉefaparato.dashboard_data.Pressure.toInt())
        val balim = Nombroj.nombrigi(ĉefaparato.dashboard_data.Noise.toInt())
        val listo = mutableListOf(
                "nevum $nevum",
                "dreta $dreta",
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
        val dreta2 = temperaturo.dashboard_data.Humidity!!

        listo.add("nevum ${Nombroj.nombrigi(nevum2.toInt())}")
        listo.add("dreta ${Nombroj.nombrigi(dreta2.toInt())}")

        val glimaSenam = pluvo.dashboard_data.Rain!!

        listo.add("glima senam ${Nombroj.nombrigi(glimaSenam.toInt())}")

        return Iloj.listigi(listo) to Certeco.Sagi
    }
}