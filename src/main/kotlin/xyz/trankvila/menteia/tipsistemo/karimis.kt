package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.temporal.ChronoUnit

abstract class karimis(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
): timis(morem, ponem, forem) {
    override val _tipo = _certeco.sagi
    protected abstract val _valuo: LocalDate

    override suspend fun _valuigi(): LocalDate {
        return _valuo
    }

    override suspend fun _simpligi(): timis {
        val dato = SilicanDate.fromGregorian(_valuo)
        return karima(
                lemis.ciferigi(dato.year.toBigInteger()),
                lemis.ciferigi(dato.month.toBigInteger()),
                lemis.ciferigi(dato.day.toBigInteger())
        )
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is karima -> {
                return runBlocking {
                    _simpligi() == other
                }
            }
            else -> super.equals(other)
        }
    }
}

class karima(morem: kamis, ponem: kamis, forem: kamis): sadimis<kamis, kamis, kamis>(morem, ponem, forem) {
    companion object {
        fun igi(date: LocalDate): karima {
            val dato = SilicanDate.fromGregorian(date)
            return karima(
                    lemis.ciferigi(dato.year.toBigInteger()),
                    lemis.ciferigi(dato.month.toBigInteger()),
                    lemis.ciferigi(dato.day.toBigInteger())
            )
        }
    }
}

class fidinas: karimis() {
    override val _valuo = LocalDate.now()
}

class fitam(morem: lemis): karimis(morem) {
    override val _valuo: LocalDate =
            runBlocking {
                LocalDate.now().plusDays(morem._valuigi().numerator.toLong())
            }
}

val syncPoint = LocalDate.of(2018, 1, 1)
const val syncDayNumber = 12018 * 364 + 7 * ((12018 / 5) - (12018 / 40) + (12018 / 400))
const val daysIn400Years = 400 * 364 + 7 * (400 / 5 - 400 / 40 + 1)
const val daysIn40Years = 40 * 364 + 7 * (40 / 5 - 1)
const val daysIn5Years = 5 * 364 + 7

data class SilicanDate(val year: Int, val month: Int, val day: Int) {
    companion object {
        fun fromGregorian(date: LocalDate): SilicanDate {
            val difference = ChronoUnit.DAYS.between(syncPoint, date)
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