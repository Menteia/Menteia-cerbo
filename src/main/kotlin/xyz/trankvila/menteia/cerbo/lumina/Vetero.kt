package xyz.trankvila.menteia.cerbo.lumina

import xyz.trankvila.menteia.datumo.*
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.Certeco
import xyz.trankvila.menteia.cerbo.MenteiaEksepcio
import xyz.trankvila.menteia.cerbo.Iloj
import xyz.trankvila.menteia.cerbo.kiram.Nombroj
import java.util.*
import kotlin.math.roundToInt

object Vetero {
    suspend fun lemona(arbo: SintaksoArbo): Pair<String, Certeco> {
        val lokoNomo = arbo.radiko
        val lokoID = VeteraDatumo.lokoj[lokoNomo] ?: throw Exception("$lokoNomo ne ekzistas")
        val raporto = alirilaro.getCurrentWeather(lokoID)
        val lumina = VeteraDatumo.kodoj[raporto.weather[0].id]
                ?: throw Exception("${raporto.weather[0].id} ne ekzistas")
        val raportenhavo = "sadika ${lumina} nevum ${Nombroj.nombrigi(raporto.main.temp.roundToInt())} posetim ${Nombroj.nombrigi((raporto.wind.speed * 3.6).roundToInt())}"
        return raportenhavo to Certeco.Sagi
    }

    suspend fun lurina(lokoArbo: SintaksoArbo, datoArbo: SintaksoArbo): Pair<String, Certeco> {
        val lokoNomo = lokoArbo.radiko
        val lokoID = VeteraDatumo.lokoj[lokoNomo] ?: throw MenteiaEksepcio("klos tinas $lokoArbo")
        val dato = Iloj.legiDaton(datoArbo)
        dato.set(Calendar.HOUR_OF_DAY, 12)
        val raporto = alirilaro.getForecast(lokoID, dato) ?: throw MenteiaEksepcio("klos tinas lurina $lokoArbo $datoArbo")
        val lumina = VeteraDatumo.kodoj[raporto.weather[0].id]
                ?: throw Exception("${raporto.weather[0].id} ne ekzistas")
        return "sadika ${lumina} nevum ${Nombroj.nombrigi(raporto.main.temp.roundToInt())} posetim ${Nombroj.nombrigi((raporto.wind.speed * 3.6).roundToInt())}" to Certeco.Sagi
    }
}