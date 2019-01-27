package xyz.`5atm`.menteia.datumo

import awaitStringResponse
import com.github.kittinunf.fuel.Fuel
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
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
        val humidity: Byte,
        val hvac_mode: String
)

val hvacModes = mapOf(
        "heat" to "saresa",
        "cool" to "silega",
        "heat-cool" to "sasigas",
        "eco" to "maraga",
        "off" to "buve"
)

suspend fun getThermostatAlt(id: String): ThermostatState {
    HttpClient(Apache).use {
        val respondo = it.get<String>(
                URL("https://developer-api.nest.com/devices/thermostats/${Sekretoj.NestDeviceIDs[id]}")
        ) {
            headers {
                append("Authorization", Sekretoj.NestKey)
            }
        }
        return JSON.nonstrict.parse(ThermostatState.serializer(), respondo)
    }
}