package xyz.`5atm`.menteia.cerbo.lumina

import xyz.`5atm`.menteia.datumo.*
import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.Certeco
import xyz.`5atm`.menteia.cerbo.Iloj
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import java.util.*
import kotlin.math.roundToInt

object Vetero {
    suspend fun lemona(arbo: SintaksoArbo): Pair<String, Certeco> {
        val lokoNomo = arbo.radiko
        val lokoID = VeteraDatumo.lokoj[lokoNomo] ?: throw Exception("$lokoNomo ne ekzistas")
        val raporto = current(lokoID)
        val lumina = VeteraDatumo.kodoj[raporto.weather[0].id]
                ?: throw Exception("${raporto.weather[0].id} ne ekzistas")
        val raportenhavo = "luvana ${lumina} nevum ${Nombroj.nombrigi(raporto.main.temp.roundToInt())} posetim ${Nombroj.nombrigi((raporto.wind.speed * 3.6).roundToInt())}"
        return raportenhavo to Certeco.Sagi
    }

    suspend fun lurina(lokoArbo: SintaksoArbo, datoArbo: SintaksoArbo): Pair<String, Certeco> {
        val lokoNomo = lokoArbo.radiko
        val lokoID = VeteraDatumo.lokoj[lokoNomo] ?: throw Exception("$lokoNomo ne ekzistas")
        val dato = Iloj.legiDaton(datoArbo)
        dato.set(Calendar.HOUR_OF_DAY, 12)
        val raporto = forecast(lokoID, dato) ?: throw Exception("Neniu datumo por $datoArbo")
        val lumina = VeteraDatumo.kodoj[raporto.weather[0].id]
                ?: throw Exception("${raporto.weather[0].id} ne ekzistas")
        println(raporto)
        return "luvana ${lumina} nevum ${Nombroj.nombrigi(raporto.main.temp.roundToInt())} posetim ${Nombroj.nombrigi((raporto.wind.speed * 3.6).roundToInt())}" to Certeco.Sagi
    }
}