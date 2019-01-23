package xyz.`5atm`.menteia.datumo

import awaitString
import awaitStringResponse
import awaitStringResult
import com.github.kittinunf.fuel.Fuel
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import java.lang.Exception

@Serializable
data class Main(
        val temp: Float,
        val temp_min: Float,
        val temp_max: Float,
        val pressure: Float,
        @Optional val sea_level: Float? = null,
        @Optional val grnd_level: Float? = null,
        val humidity: Float,
        @Optional val temp_kf: Float? = null
)

@Serializable
data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
)

@Serializable
data class Clouds(val all: Float)

@Serializable
data class Wind(val speed: Float, val deg: Float)

@Serializable
data class Snow(val `3h`: Float)

@Serializable
data class Rain(val `3h`: Float)

@Serializable
data class Raportano(
        val dt: Int,
        val main: Main,
        val weather: Array<Weather>,
        val clouds: Clouds,
        val wind: Wind,
        @Optional val snow: Snow? = null,
        @Optional val rain: Rain? = null,
        @Optional val dt_txt: String? = null
)

@Serializable
data class Raporto(val list: Array<Raportano>)

internal suspend fun current(loko: Int): Raportano {
    val (request, response, result) = Fuel.get(
            "https://api.openweathermap.org/data/2.5/weather",
            listOf("id" to loko, "appid" to Sekretoj.OpenWeatherMapKey, "units" to "metric")
    ).awaitStringResponse()
    return result.fold(
            { data -> JSON.nonstrict.parse(Raportano.serializer(), data) },
            { error -> throw error.exception }
    )
}