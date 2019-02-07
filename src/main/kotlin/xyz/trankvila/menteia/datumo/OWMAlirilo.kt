package xyz.trankvila.menteia.datumo

import awaitStringResponse
import com.github.kittinunf.fuel.Fuel
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import java.util.*

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
data class Snow(@Optional val `3h`: Float = 0F)

@Serializable
data class Rain(@Optional val `3h`: Float = 0F)

@Serializable
data class WeatherReport(
        val dt: Long,
        val main: Main,
        val weather: Array<Weather>,
        val clouds: Clouds,
        val wind: Wind,
        @Optional val snow: Snow? = null,
        @Optional val rain: Rain? = null,
        @Optional val dt_txt: String? = null
)

@Serializable
data class Raporto(val list: Array<WeatherReport>)