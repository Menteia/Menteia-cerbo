package xyz.trankvila.menteia.datumo

import awaitStringResponse
import com.github.kittinunf.fuel.Fuel
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.put
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

@Serializable
data class ThermostatState(
        val name: String,
        val ambient_temperature_c: Float,
        @Optional val target_temperature_c: Float? = null,
        @Optional val target_temperature_high_c: Float? = null,
        @Optional val target_temperature_low_c: Float? = null,
        val humidity: Byte,
        val hvac_state: String,
        val hvac_mode: String
)

val hvacStates = mapOf(
        "heating" to "saresa",
        "cooling" to "silega",
        "off" to "buve"
)

val hvacModes = mapOf(
        "heat" to "saresa",
        "cool" to "silega",
        "heat-cool" to "sasigas",
        "eco" to "maraga",
        "off" to "buve"
)

val hvacModesReversed = hvacModes.map {
    it.value to it.key
}.toMap()

val properties = mapOf(
        "sevara" to "target_temperature_c"
)