package xyz.trankvila.menteia.datumo

import java.math.BigDecimal
import java.util.*

interface Alirilaro {
    fun alportiListon(nomo: String): List<String>
    fun nombriListojn(): Int
    fun redaktiListon(nomo: String, enhavo: List<String>)
    fun ĉiujListoj(): Map<String, List<String>>
    fun kreiNomon(tipo: String): String
    fun kreiListon(tipo: String): String
    fun forigiListon(nomo: String)
    fun forigiTempoŝaltilon(nomo: String)

    suspend fun getThermostatState(id: String): ThermostatState
    suspend fun setThermostatTemperature(id: String, targetTemperature: BigDecimal)
    suspend fun setThermostatMode(id: String, mode: String, t1: BigDecimal? = null, t2: BigDecimal? = null)

    suspend fun getWeatherStationState(): WeatherStationState

    suspend fun getCurrentWeather(location: Int): WeatherReport
    suspend fun getForecast(location: Int, date: Calendar): WeatherReport?

    suspend fun getLightBulbState(name: String): LightBulbState
    suspend fun setLightBulbOn(name: String, on: Boolean)
    suspend fun setLightBulbBrightness(name: String, brightness: BigDecimal)
}

var alirilaro = RealaAlirilaro