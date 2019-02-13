package xyz.trankvila.menteia.datumo

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class WeatherItemsType(
        val daylight: String,
        val daySegment: String,
        val description: String,
        val skyInfo: String,
        val skyDescription: String,
        val temperature: String,
        val temperatureDesc: String,
        val comfort: String,
        val highTemperature: String,
        val lowTemperature: String,
        val humidity: String,
        val dewPoint: String,
        val precipitation1H: String,
        val precipitation3H: String,
        val precipitation6H: String,
        val precipitation12H: String,
        val precipitation24H: String,
        val precipitationProbability: String,
        val precipitationDesc: String,
        @Optional val rainFall: String? = null,
        @Optional val snowFall: String? = null,
        val snowCover: String,
        val airInfo: String,
        val airDescription: String,
        val windSpeed: String,
        val windDirection: String,
        val windDesc: String,
        val windDescShort: String,
        @Optional val beaufortScale: String? = null,
        @Optional val beaufortDescription: String? = null,
        @Optional val uvIndex: String? = null,
        @Optional val uvDesc: String? = null,
        val barometerPressure: String,
        val barometerTrend: String,
        val visibility: String,
        val iconName: String,
        val iconLink: String,
        val ageMinutes: String,
        val activeAlerts: String
)