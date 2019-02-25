package xyz.trankvila.menteia.datumo

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

interface Alirilaro {
    fun alportiListon(nomo: String): List<String>?
    fun alportiLokon(nomo: String): Pair<String, String>
    fun alportiLumon(nomo: String): Int
    fun alportiTermostaton(nomo: String): Pair<String, String>
    fun nombriListojn(): Int
    fun redaktiListon(nomo: String, enhavo: List<String>)
    fun ĉiujListoj(): Map<String, List<String>>
    fun kreiNomon(tipo: String): String
    fun kreiListon(nomo: String): String
    fun forigiListon(nomo: String)
    fun forigiTempoŝaltilon(nomo: String)

    suspend fun getThermostatState(id: String, key: String): ThermostatState
    suspend fun setThermostatTemperature(id: String, targetTemperature: BigDecimal)
    suspend fun setThermostatMode(key: String, id: String, mode: String, t1: BigDecimal? = null, t2: BigDecimal? = null)

    suspend fun getWeatherStationState(): WeatherStationState

    suspend fun getCurrentWeather(location: Pair<String, String>): WeatherItemsType
    suspend fun getForecast(location: Pair<String, String>, date: LocalDate): WeatherItemsType?

    suspend fun getLightBulbState(id: Int): LightBulbState
    suspend fun setLightBulbOn(id: Int, on: Boolean)
    suspend fun setLightBulbBrightness(name: String, brightness: BigDecimal)
}

var alirilaro = RealaAlirilaro