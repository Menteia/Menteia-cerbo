package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.cerbo.kiramis.Nombroj
import java.time.ZoneId
import java.util.*

abstract class ŝanamis(
        val _valuo: Calendar,
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): _negiTipo(morem, ponem, forem) {
    companion object {
        private val fazoj = listOf(::valima, ::darena, ::gemuna)
    }

    override suspend fun _valuigi(): Calendar {
        return _valuo
    }

    override suspend fun _simpligi(): timis {
        _valuo.timeZone = TimeZone.getTimeZone(ZoneId.of("America/Toronto"))
        val fazo = fazoj[_valuo[Calendar.HOUR_OF_DAY] / 8]
        val horo = _valuo[Calendar.HOUR_OF_DAY] % 8
        val minuto = _valuo[Calendar.MINUTE]
        return fazo(lemis.ciferigi(horo.toBigInteger()), lemis.ciferigi(minuto.toBigInteger()))
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is ŝanamis -> {
                return _valuo[Calendar.HOUR] == other._valuo[Calendar.HOUR] &&
                        _valuo[Calendar.MINUTE] == other._valuo[Calendar.MINUTE]
            }
            else -> {
                super.equals(other)
            }
        }
    }
}

class geradas: ŝanamis(
        Calendar.getInstance()
) {
    override fun toString(): String {
        return this::class.simpleName!!
    }
}

class valima(morem: kamis, ponem: kamis): ŝanamis(
        GregorianCalendar(0, 0, 0, morem._valuo.numerator.toInt(), ponem._valuo.numerator.toInt()),
        morem, ponem
)

class darena(morem: kamis, ponem: kamis): ŝanamis(
        GregorianCalendar(0, 0, 0, morem._valuo.numerator.toInt() + 8, ponem._valuo.numerator.toInt()),
        morem, ponem
)

class gemuna(morem: kamis, ponem: kamis): ŝanamis(
        GregorianCalendar(0, 0, 0, morem._valuo.numerator.toInt() + 16, ponem._valuo.numerator.toInt()),
        morem, ponem
)