package xyz.trankvila.menteia.datumo

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable

data class WeatherItemsType(
        val daylight: String,
        val daySegment: String? = null,
        val description: String,
        val skyInfo: String,
        val skyDescription: String,
        val temperature: String? = null,
        val temperatureDesc: String,
        val comfort: String,
        val highTemperature: String,
        val lowTemperature: String,
        val humidity: String,
        val dewPoint: String,
        val precipitation1H: String? = null,
        val precipitation3H: String? = null,
        val precipitation6H: String? = null,
        val precipitation12H: String? = null,
        val precipitation24H: String? = null,
        val precipitationProbability: String? = null,
        val precipitationDesc: String,
        val rainFall: String? = null,
        val snowFall: String? = null,
        val snowCover: String? = null,
        val airInfo: String,
        val airDescription: String,
        val windSpeed: String,
        val windDirection: String,
        val windDesc: String,
        val windDescShort: String,
        val beaufortScale: String? = null,
        val beaufortDescription: String? = null,
        val uvIndex: String? = null,
        val uvDesc: String? = null,
        val barometerPressure: String,
        val barometerTrend: String? = null,
        val visibility: String? = null,
        val iconName: String,
        val iconLink: String,
        val ageMinutes: String? = null,
        val activeAlerts: String? = null,
        val utcTime: String
)

data class ObservationLocationType(
        val observation: List<WeatherItemsType>
)

data class ObservationsType(
        val location: List<ObservationLocationType>
)

data class ObservationResponse(
        val observations: ObservationsType
)

data class ForecastLocationType(
        val forecast: List<WeatherItemsType>
)

data class ForecastsType(
        val forecastLocation: ForecastLocationType
)

data class ForecastResponse(
        val dailyForecasts: ForecastsType
)