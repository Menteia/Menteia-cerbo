package xyz.trankvila.menteia.cerbo.ʃanam

import xyz.trankvila.menteia.cerbo.kiram.Nombroj
import xyz.trankvila.menteia.cerbo.Certeco
import java.time.ZoneId
import java.util.*

object Tempo {
    private val fazoj = listOf("valima", "darena", "gemuna")

    fun geradas(): Pair<String, Certeco> {
        val nun = Calendar.getInstance()
        nun.timeZone = TimeZone.getTimeZone(ZoneId.of("America/Toronto"))
        val fazo = fazoj[nun[Calendar.HOUR_OF_DAY] / 8]
        val horo = nun[Calendar.HOUR_OF_DAY] % 8
        val minuto = nun[Calendar.MINUTE]
        return "$fazo ${Nombroj.nombrigi(horo)} ${Nombroj.nombrigi(minuto)}" to Certeco.Negi
    }

    fun fidinas(): Pair<String, Certeco> {
        val nun = Calendar.getInstance()
        nun.timeZone = TimeZone.getTimeZone(ZoneId.of("America/Toronto"))
        val hodiaŭ = SilicanDate.fromGregorian(nun)
        return "karima ${Nombroj.nombrigi(hodiaŭ.year)} ${Nombroj.nombrigi(hodiaŭ.month)} ${Nombroj.nombrigi(hodiaŭ.day)}" to Certeco.Negi
    }
}

val syncPoint = GregorianCalendar(2018, 0, 1)
const val syncDayNumber = 12018 * 364 + 7 * ((12018 / 5) - (12018 / 40) + (12018 / 400))
const val daysIn400Years = 400 * 364 + 7 * (400 / 5 - 400 / 40 + 1)
const val daysIn40Years = 40 * 364 + 7 * (40 / 5 - 1)
const val daysIn5Years = 5 * 364 + 7

data class SilicanDate(val year: Int, val month: Int, val day: Int) {
    companion object {
        fun fromGregorian(date: Calendar): SilicanDate {
            val difference = (date.timeInMillis - syncPoint.timeInMillis) / (1000 * 60 * 60 * 24)
            val dayNumber = syncDayNumber + difference
            val years400 = dayNumber / daysIn400Years
            val remain400 = dayNumber % daysIn400Years
            val years40 = remain400 / daysIn40Years
            val remain40 = remain400 % daysIn40Years
            val years5 = remain40 / daysIn5Years
            val remain5 = remain40 % daysIn5Years
            val remainingYears = remain5 / 364
            val remainingDays = remain5 % 364
            val year = years400 * 400 + years40 * 40 + years5 * 5 + Math.min(remainingYears, 5)
            val dayOfYear = if (remainingYears == 6L) 364 + remainingDays else remainingDays
            val month = (dayOfYear / 28).toInt()
            val day = (dayOfYear % 28).toInt() + 1
            return SilicanDate(year.toInt(), Math.min(month + 1, 13), if (month == 13) day + 28 else day)
        }
    }
}