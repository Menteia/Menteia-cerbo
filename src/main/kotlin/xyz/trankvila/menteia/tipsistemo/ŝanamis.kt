package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.cerbo.Certeco
import xyz.trankvila.menteia.cerbo.kiramis.Nombroj
import java.time.ZoneId
import java.util.*

abstract class ŝanamis(
        val _valuo: Calendar,
        morem: timis? = null,
        ponem: timis? = null,
        forem: timis? = null
): timis(morem, ponem, forem) {
    companion object {
        private val fazoj = listOf("valima", "darena", "gemuna")
    }

    override fun _valuigi(): Calendar {
        return _valuo
    }

    override fun toString(): String {
        _valuo.timeZone = TimeZone.getTimeZone(ZoneId.of("America/Toronto"))
        val fazo = fazoj[_valuo[Calendar.HOUR_OF_DAY] / 8]
        val horo = _valuo[Calendar.HOUR_OF_DAY] % 8
        val minuto = _valuo[Calendar.MINUTE]
        return "$fazo ${Nombroj.nombrigi(horo)} ${Nombroj.nombrigi(minuto)}"
    }
}

class geradas: ŝanamis(
        Calendar.getInstance()
)