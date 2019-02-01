package xyz.`5atm`.menteia.datumo

import java.util.*

interface Alirilaro {
    fun alportiListon(nomo: String): List<String>
    fun nombriListojn(): Int
    fun redaktiListon(nomo: String, enhavo: List<String>)
    fun ĉiujListoj(): Map<String, List<String>>
    fun kreiNomon(): String
    fun kreiListon(): String
    fun forigiListon(nomo: String)

    suspend fun getThermostatState(id: String): ThermostatState
    suspend fun setThermostatTemperature(id: String, targetTemperature: Int)

    suspend fun getWeatherStationState(): WeatherStationState

    suspend fun getCurrentWeather(location: Int): WeatherReport
    suspend fun getForecast(location: Int, date: Calendar): WeatherReport?
}

var alirilaro = RealaAlirilaro