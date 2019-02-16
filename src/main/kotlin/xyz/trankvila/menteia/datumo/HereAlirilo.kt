package xyz.trankvila.menteia.datumo

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

@Serializable
data class WeatherItemsType(
        val daylight: String,
        @Optional val daySegment: String? = null,
        val description: String,
        val skyInfo: String,
        val skyDescription: String,
        @Optional val temperature: String? = null,
        val temperatureDesc: String,
        val comfort: String,
        val highTemperature: String,
        val lowTemperature: String,
        val humidity: String,
        val dewPoint: String,
        @Optional val precipitation1H: String? = null,
        @Optional val precipitation3H: String? = null,
        @Optional val precipitation6H: String? = null,
        @Optional val precipitation12H: String? = null,
        @Optional val precipitation24H: String? = null,
        @Optional val precipitationProbability: String? = null,
        val precipitationDesc: String,
        @Optional val rainFall: String? = null,
        @Optional val snowFall: String? = null,
        @Optional val snowCover: String? = null,
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
        @Optional val barometerTrend: String? = null,
        val visibility: String,
        val iconName: String,
        val iconLink: String,
        @Optional val ageMinutes: String? = null,
        val activeAlerts: String,
        val utcTime: String
)

@Serializable
data class ObservationLocationType(
        val observation: List<WeatherItemsType>
)

@Serializable
data class ObservationsType(
        val location: List<ObservationLocationType>
)

@Serializable
data class ObservationResponse(
        val observations: ObservationsType
)

@Serializable
data class ForecastLocationType(
        val forecast: List<WeatherItemsType>
)

@Serializable
data class ForecastsType(
        val forecastLocation: ForecastLocationType
)

@Serializable
data class ForecastResponse(
        val dailyForecasts: ForecastsType
)