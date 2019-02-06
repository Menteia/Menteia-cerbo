package xyz.`5atm`.menteia.datumo

import java.util.*

interface Alirilaro {
    fun alportiListon(nomo: String): List<String>
    fun nombriListojn(): Int
    fun redaktiListon(nomo: String, enhavo: List<String>)
    fun ĉiujListoj(): Map<String, List<String>>
    fun kreiNomon(tipo: String): String
    fun kreiListon(): String
    fun forigiListon(nomo: String)
    fun forigiTempoŝaltilon(nomo: String)

    suspend fun getThermostatState(id: String): ThermostatState
    suspend fun setThermostatTemperature(id: String, targetTemperature: Int)
    suspend fun setThermostatMode(id: String, mode: String, t1: Int? = null, t2: Int? = null)

    suspend fun getWeatherStationState(): WeatherStationState

    suspend fun getCurrentWeather(location: Int): WeatherReport
    suspend fun getForecast(location: Int, date: Calendar): WeatherReport?
}

var alirilaro = RealaAlirilaro