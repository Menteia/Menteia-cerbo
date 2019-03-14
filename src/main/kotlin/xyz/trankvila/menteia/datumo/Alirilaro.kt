package xyz.trankvila.menteia.datumo

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

interface Alirilaro {
    suspend fun alportiListon(nomo: String): List<String>?
    suspend fun alportiLokon(nomo: String): Pair<String, String>
    suspend fun alportiLumon(nomo: String): Int
    suspend fun alportiTermostaton(nomo: String): Pair<String, String>
    suspend fun nombri(tipo: String): Int
    suspend fun nomi(tipo: String): List<String>
    suspend fun redaktiListon(nomo: String, enhavo: List<String>)
    suspend fun ĉiujListoj(): Map<String, List<String>>
    suspend fun kreiNomon(tipo: String): String
    suspend fun kreiListon(nomo: String): String
    suspend fun forigiListon(nomo: String)
    suspend fun forigiTempoŝaltilon(nomo: String)
    suspend fun kreiEventon(nomo: String, id: String)
    suspend fun alportiEventon(nomo: String): String
    suspend fun forigiEventon(nomo: String)

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