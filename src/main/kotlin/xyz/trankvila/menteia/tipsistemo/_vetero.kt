package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.datumo.VeteraDatumo.hereKodoj
import xyz.trankvila.menteia.datumo.WeatherItemsType
import xyz.trankvila.menteia.datumo.alirilaro
import java.math.BigDecimal
import java.time.LocalDate

abstract class _vetero(
        val _loko: Pair<String, String>,
        val _dato: LocalDate? = null,
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): _sagiTipo(morem, ponem, forem)

class lemona(morem: sinemis): _vetero(alirilaro.alportiLokon(morem._nomo), morem = morem) {
    override suspend fun _valuigi(): WeatherItemsType {
        return alirilaro.getCurrentWeather(_loko)
    }

    override suspend fun _simpligi(): timis {
        val rezulto = _valuigi()
        return sadika(
                hereKodoj.getValue(rezulto.iconName),
                nevum(lemis.nombrigi(BigDecimal(rezulto.temperature), 1)),
                nevum(lemis.nombrigi(BigDecimal(rezulto.comfort), 1))
        )
    }
}

class lurina(morem: sinemis, ponem: karimis): _vetero(
        alirilaro.alportiLokon(morem._nomo),
        ponem._valuo,
        morem,
        ponem
) {
    override suspend fun _valuigi(): WeatherItemsType? {
        return alirilaro.getForecast(_loko, _dato!!)
    }

    override suspend fun _simpligi(): timis? {
        val rezulto = _valuigi() ?: return null
        return sadika(
                hereKodoj.getValue(rezulto.iconName),
                nevum(lemis.nombrigi(rezulto.highTemperature.toBigDecimal(), 1)),
                nevum(lemis.nombrigi(rezulto.lowTemperature.toBigDecimal(), 1))
        )
    }
}