package xyz.trankvila.menteia.datumo

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.BadResponseStatusException
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.Parameters
import io.ktor.http.formUrlEncode
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.net.URL

internal val logger = LoggerFactory.getLogger("NetatmoAlirilo")

@Serializable
data class ModuleDashboardData(
        @Optional val Rain: Float? = null,
        @Optional val sum_rain_24: Float? = null,
        @Optional val sum_rain_1: Float? = null,
        @Optional val Temperature: Float? = null,
        @Optional val Humidity: Float? = null
)

@Serializable
data class Module(
        val _id: String,
        val type: String,
        val module_name: String,
        val dashboard_data: ModuleDashboardData
)

@Serializable
data class MainDashboardData(
        val Temperature: Float,
        val CO2: Float,
        val Humidity: Float,
        val Noise: Float,
        val Pressure: Float
)

@Serializable
data class Device(
        val _id: String,
        val type: String,
        val module_name: String,
        val station_name: String,
        val dashboard_data: MainDashboardData,
        val modules: Array<Module>
)

@Serializable
data class Body(
        val devices: Array<Device>
)

@Serializable
data class WeatherStationState(
        val body: Body
)

@Serializable
data class AuthenticationResponse(
        val access_token: String,
        val refresh_token: String
)

suspend fun authenticate(): String {
    try {
        HttpClient(Apache).use {
            val response = it.post<String>(URL("https://api.netatmo.com/oauth2/token")) {
                val params = Parameters.build {
                    val values = Sekretoj.NetatmoCredentials()
                    append("client_id", values.getValue("/External/Netatmo/ClientID"))
                    append("client_secret", values.getValue("/External/Netatmo/ClientSecret"))
                    append("grant_type", "password")
                    append("username", values.getValue("/External/Netatmo/Username"))
                    append("password", values.getValue("/External/Netatmo/Password"))
                }
                body = FormDataContent(params)
            }
            val datumo = JSON.nonstrict.parse(AuthenticationResponse.serializer(), response)
            Sekretoj.session["NetatmoAccessToken"] = datumo.access_token
            Sekretoj.session["NetatomRefreshToken"] = datumo.refresh_token
            return datumo.access_token
        }
    } catch (e: BadResponseStatusException) {
        logger.error(e.response.content.readUTF8Line())
        throw e
    }
}