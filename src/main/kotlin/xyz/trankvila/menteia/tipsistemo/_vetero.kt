package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import xyz.trankvila.menteia.datumo.VeteraDatumo.hereKodoj
import xyz.trankvila.menteia.datumo.WeatherItemsType
import xyz.trankvila.menteia.datumo.alirilaro
import java.math.BigDecimal
import java.time.LocalDate

abstract class _vetero(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
): timis(morem, ponem, forem) {
    override val _tipo = _certeco.sagi
    protected abstract val _loko: Deferred<Pair<String, String>>
    protected abstract val _dato: LocalDate?
}

class lemona(_loko: sinemis): _vetero(_loko) {
    override val _loko: Deferred<Pair<String, String>> = GlobalScope.async {
        alirilaro.alportiLokon(_loko._nomo)
    }

    override val _dato: LocalDate? = null

    override suspend fun _valuigi(): WeatherItemsType {
        return alirilaro.getCurrentWeather(_loko.await())
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

class lurina(_loko: sinemis, _dato: karimis): _vetero(_loko, _dato) {
    override val _loko: Deferred<Pair<String, String>> = GlobalScope.async {
        alirilaro.alportiLokon(_loko._nomo)
    }
    override val _dato = runBlocking { _dato._valuigi() }
    override suspend fun _valuigi(): WeatherItemsType? {
        return alirilaro.getForecast(_loko.await(), _dato)
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