package xyz.`5atm`.menteia.cerbo.lumina

import xyz.`5atm`.menteia.datumo.*
import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.Iloj
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import kotlin.math.roundToInt

object Vetero {
    suspend fun lemona(arbo: SintaksoArbo): String {
        val lokoNomo = arbo.radiko
        val lokoID = VeteraDatumo.lokoj[lokoNomo] ?: throw Exception("$lokoNomo ne ekzistas")
        val raporto = current(lokoID)
        val lumina = VeteraDatumo.kodoj[raporto.weather[0].id]
                ?: throw Exception("${raporto.weather[0].id} ne ekzistas")
        val raportenhavo = "luvana ${lumina} nevum ${Nombroj.nombrigi(raporto.main.temp.roundToInt())} posetim ${Nombroj.nombrigi((raporto.wind.speed * 3.6).roundToInt())}"
        return raportenhavo
    }
}