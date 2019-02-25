package xyz.trankvila.menteia.datumo

import awaitStringResponse
import com.github.kittinunf.fuel.Fuel
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.BadResponseStatusException
import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JSON
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import software.amazon.awssdk.services.lambda.LambdaClient
import software.amazon.awssdk.services.lambda.model.InvokeRequest
import software.amazon.awssdk.services.ssm.SsmClient
import software.amazon.awssdk.services.ssm.model.GetParametersRequest
import software.amazon.awssdk.services.ssm.model.ParameterType
import java.lang.Exception
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object RealaAlirilaro : Alirilaro {
    private val db = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build()
    private val lambda = LambdaClient.builder()
            .region(Region.US_EAST_1)
            .build()
    val ssm = SsmClient.builder()
            .region(Region.US_WEST_2)
            .build()

    override fun alportiListon(nomo: String): List<String>? {
        val respondo = db.query(QueryRequest.builder()
                .tableName("Menteia-datumejo")
                .expressionAttributeValues(mapOf(":nomo" to AttributeValue.builder().s(nomo).build()))
                .keyConditionExpression("vorto = :nomo")
                .build()
        )
        if (respondo.count() != 1) {
            throw Exception("Ne eblis trovi la liston nomita ${nomo}")
        }
        return respondo.items()[0]["enhavo"]?.l()?.map {
            it.s()
        }
    }

    override fun alportiLokon(nomo: String): Pair<String, String> {
        val respondo = db.query(QueryRequest.builder()
                .tableName("Menteia-datumejo")
                .keyConditionExpression("vorto = :nomo")
                .expressionAttributeValues(mapOf(
                        ":nomo" to AttributeValue.builder().s(nomo).build()
                ))
                .build()
        )
        if (respondo.count() != 1) {
            throw Exception("Ne eblis trovi la lokon $nomo")
        }
        val loko = respondo.items().first()
        if (loko["tipo"]!!.s() != "sinemis") {
            throw Exception("$nomo ne estas loko")
        }
        val koordinatoj = loko["valuo"]!!.l()
        return koordinatoj[0].n() to koordinatoj[1].n()
    }

    override fun alportiLumon(nomo: String): Int {
        val respondo = db.query(QueryRequest.builder()
                .tableName("Menteia-datumejo")
                .keyConditionExpression("vorto = :nomo")
                .filterExpression("tipo = :tipo")
                .expressionAttributeValues(mapOf(
                        ":nomo" to AttributeValue.builder().s(nomo).build(),
                        ":tipo" to AttributeValue.builder().s("milimis").build()
                ))
                .build()
        )
        if (respondo.count() != 1) {
            throw Exception("Ne eblis trovi la lumon $nomo")
        }
        val loko = respondo.items().first()
        val id = loko["valuo"]!!.n()
        return id.toInt()
    }

    override fun alportiTermostaton(nomo: String): Pair<String, String> {
        val respondo = db.query(QueryRequest.builder()
                .tableName("Menteia-datumejo")
                .keyConditionExpression("vorto = :nomo")
                .filterExpression("tipo = :tipo")
                .expressionAttributeValues(mapOf(
                        ":nomo" to AttributeValue.builder().s(nomo).build(),
                        ":tipo" to AttributeValue.builder().s("kredimis").build()
                ))
                .build()
        )
        if (respondo.count() != 1) {
            throw Exception("Ne eblis trovi la termostaton $nomo")
        }
        val valuo = respondo.items().first()["valuo"]!!.l()
        return valuo[0].s() to valuo[1].s()
    }

    override fun nombriListojn(): Int {
        val respondo = db.scan(ScanRequest.builder()
                .tableName("Menteia-datumejo")
                .filterExpression("begins_with(tipo, :nomo)")
                .expressionAttributeValues(mapOf(
                        ":nomo" to AttributeValue.builder().s("brodimis").build()
                ))
                .select(Select.COUNT)
                .build())
        return respondo.count()
    }

    override fun redaktiListon(nomo: String, enhavo: List<String>) {
        db.updateItem(UpdateItemRequest.builder()
                .tableName("Menteia-datumejo")
                .key(mapOf(
                        "vorto" to AttributeValue.builder().s(nomo).build()
                ))
                .updateExpression("set enhavo = :e")
                .expressionAttributeValues(mapOf(
                        ":e" to AttributeValue.builder().l(
                                enhavo.map {
                                    AttributeValue.builder().s(it).build()
                                }
                        ).build()
                )).build()
        )
    }

    override fun ĉiujListoj(): Map<String, List<String>> {
        val listoj = mutableMapOf<String, List<String>>()
        val respondo = db.scanPaginator(ScanRequest.builder()
                .tableName("Menteia-datumejo")
                .filterExpression("begins_with(tipo, :nomo) and attribute_exists(enhavo)")
                .expressionAttributeValues(mapOf(
                        ":nomo" to AttributeValue.builder().s("brodimis").build()
                ))
                .build()
        )
        respondo.forEach {
            it.items().forEach {
                listoj[it["vorto"]!!.s()] = it["enhavo"]!!.l().map {
                    it.s()
                }
            }
        }
        return listoj
    }

    override fun kreiNomon(tipo: String): String {
        funkcio@ while (true) {
            val respondo = lambda.invoke(InvokeRequest.builder()
                    .functionName("Menteia-vortilo")
                    .build())
                    .payload()
                    .asUtf8String()
            val vorto = respondo.substring(1..respondo.length-2)
            for (v in Vortaro.alporti()) {
                if (v.key.startsWith(vorto) || vorto.startsWith(v.key) ||
                        v.key.endsWith(vorto) || vorto.endsWith(v.key)) {
                    continue@funkcio
                }
            }
            try {
                db.putItem(PutItemRequest.builder()
                        .tableName("Menteia-datumejo")
                        .conditionExpression("attribute_not_exists(vorto)")
                        .item(mapOf(
                                "vorto" to AttributeValue.builder().s(vorto).build(),
                                "tipo" to AttributeValue.builder().s(tipo).build()
                        ))
                        .build())
                Vortaro.alporti(alporti = true)
                return vorto
            } catch (e: ConditionalCheckFailedException) {
                continue
            }
        }
    }

    override fun kreiListon(nomo: String): String {
        db.updateItem(UpdateItemRequest.builder()
                .tableName("Menteia-datumejo")
                .key(mapOf(
                        "vorto" to AttributeValue.builder().s(nomo).build()
                ))
                .updateExpression("set valuo = :e")
                .expressionAttributeValues(mapOf(
                        ":e" to AttributeValue.builder().l(listOf()).build()
                ))
                .build())
        Vortaro.alporti(alporti = true)
        return nomo
    }

    override fun forigiListon(nomo: String) {
        db.deleteItem(DeleteItemRequest.builder()
                .tableName("Menteia-datumejo")
                .conditionExpression("begins_with(tipo, :t) and attribute_exists(enhavo)")
                .expressionAttributeValues(mapOf(
                        ":t" to AttributeValue.builder().s("brodimis").build()
                ))
                .key(mapOf(
                        "vorto" to AttributeValue.builder().s(nomo).build()
                ))
                .build()
        )
        Vortaro.alporti(alporti = true)
    }

    override fun forigiTempoŝaltilon(nomo: String) {
        db.deleteItem(DeleteItemRequest.builder()
                .tableName("Menteia-datumejo")
                .conditionExpression("tipo = :t")
                .expressionAttributeValues(mapOf(
                        ":t" to AttributeValue.builder().s("sanimis").build()
                ))
                .key(mapOf(
                        "vorto" to AttributeValue.builder().s(nomo).build()
                ))
                .build()
        )
        Vortaro.alporti(alporti = true)
    }

    override suspend fun getThermostatState(id: String, key: String): ThermostatState {
        HttpClient(Apache).use {
            val respondo = it.get<String>(
                    URL("https://developer-api.nest.com/devices/thermostats/$id")
            ) {
                headers {
                    append("Authorization", "Bearer $key")
                }
            }
            return JSON.nonstrict.parse(ThermostatState.serializer(), respondo)
        }
    }

    override suspend fun setThermostatTemperature(id: String, targetTemperature: BigDecimal) {
        HttpClient(Apache).use {
            it.put<String>(URL("https://developer-api.nest.com/devices/thermostats/${Sekretoj.NestDeviceID(id)}")) {
                headers {
                    append("Authorization", "Bearer ${Sekretoj.NestKey(id)}")
                }
                body = TextContent("{\"target_temperature_c\": ${targetTemperature.toPlainString()}", ContentType.Application.Json)
            }
        }
    }

    override suspend fun setThermostatMode(key: String, id: String, mode: String, t1: BigDecimal?, t2: BigDecimal?) {
        val additionalBodyContent = when (mode) {
            "heat", "cool" -> "{\"target_temperature_c\": ${t1!!.toPlainString()}}"
            "heat-cool" -> "{\"target_temperature_low_c\": ${t1!!.toPlainString()}, \"target_temperature_high_c\": ${t2!!.toPlainString()}}"
            else -> null
        }
        HttpClient(Apache).use {
            try {
                it.put<String>(URL("https://developer-api.nest.com/devices/thermostats/$id")) {
                    headers {
                        append("Authorization", "Bearer $key")
                    }
                    body = TextContent("{\"hvac_mode\": \"$mode\"}", ContentType.Application.Json)
                }
                if (additionalBodyContent != null){
                    it.put<String>(URL("https://developer-api.nest.com/devices/thermostats/${Sekretoj.NestDeviceID(id)}")) {
                        headers {
                            append("Authorization", "Bearer ${Sekretoj.NestKey(id)}")
                        }
                        body = TextContent(additionalBodyContent, ContentType.Application.Json)
                    }
                }
            } catch (e: BadResponseStatusException) {
                println(e.response.content.readUTF8Line())
                throw e
            }
        }
    }

    override suspend fun getWeatherStationState(): WeatherStationState {
        val accessToken = Sekretoj.session["NetatmoAccessToken"] ?: authenticate()
        HttpClient(Apache).use {
            val response = it.get<String>(URL("https://api.netatmo.com/api/getstationsdata")) {
                parameter("access_token", accessToken)
            }
            return JSON.nonstrict.parse(WeatherStationState.serializer(), response)
        }
    }

    override suspend fun getCurrentWeather(location: Pair<String, String>): WeatherItemsType {
        HttpClient(Apache).use {
            val response = it.get<String>(
                    URL("https://weather.api.here.com/weather/1.0/report.json")
            ) {
                val (appid, appcode) = Sekretoj.HereCredentials
                parameter("app_id", appid)
                parameter("app_code", appcode)
                parameter("product", "observation")
                parameter("oneobservation", true)
                parameter("latitude", location.first)
                parameter("longitude", location.second)
            }
            val parsed = JSON.nonstrict.parse(ObservationResponse.serializer(), response)
            return parsed.observations.location[0].observation[0]
        }
    }

    override suspend fun getForecast(location: Pair<String, String>, date: LocalDate): WeatherItemsType? {
        HttpClient(Apache).use {
            val response = it.get<String>(
                    URL("https://weather.api.here.com/weather/1.0/report.json")
            ) {
                val (appid, appcode) = Sekretoj.HereCredentials
                parameter("app_id", appid)
                parameter("app_code", appcode)
                parameter("product", "forecast_7days_simple")
                parameter("oneobservation", true)
                parameter("latitude", location.first)
                parameter("longitude", location.second)
            }
            val parsed: ForecastResponse = JSON.nonstrict.parse(ForecastResponse.serializer(), response)
            val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
            parsed.dailyForecasts.forecastLocation.forecast.forEach {
                if (LocalDate.parse(it.utcTime, format) == date) {
                    return it
                }
            }
            return null
        }
    }

    override suspend fun getLightBulbState(id: Int): LightBulbState {
        HttpClient(Apache).use {
            val response = it.get<String>("https://api.meethue.com/bridge/${Sekretoj.HueUsername()}/lights/$id") {
                headers {
                    append("Authorization", "Bearer ${Sekretoj.HueToken()}")
                }
            }
            return JSON.nonstrict.parse(HueResponse.serializer(), response).state
        }
    }

    override suspend fun setLightBulbOn(id: Int, on: Boolean) {
        HttpClient(Apache).use {
            it.put<String>("https://api.meethue.com/bridge/${Sekretoj.HueUsername()}/lights/$id/state") {
                headers {
                    append("Authorization", "Bearer ${Sekretoj.HueToken()}")
                }
                body = TextContent("{\"on\": $on}", ContentType.Application.Json)
            }
        }
    }

    override suspend fun setLightBulbBrightness(name: String, brightness: BigDecimal) {
        HttpClient(Apache).use {
            it.put<String>("https://api.meethue.com/bridge/${Sekretoj.HueUsername()}/lights/${
            Sekretoj.HueLightID(name)
            }/state") {
                headers {
                    append("Authorization", "Bearer ${Sekretoj.HueToken()}")
                }
                body = TextContent(if (brightness == BigDecimal.ZERO) {
                    "{\"on\": false}"
                } else {
                    "{\"on\": true, \"bri\": ${(brightness * BigDecimal(254)).toInt()}}"
                }, ContentType.Application.Json)
                println(body.toString())
            }
        }
    }

    suspend fun refreshHueToken() {
        val response = ssm.getParameters(
                GetParametersRequest.builder()
                        .names(
                                "/External/Hue/drakoni/RefreshToken",
                                "/External/Hue/drakoni/Authorization"
                        )
                        .withDecryption(true)
                        .build()
        )
        val refreshToken = response.parameters()[1].value()
        val authorization = response.parameters()[0].value()

        HttpClient(Apache).use {
            try {
                val update = it.post<String>("https://api.meethue.com/oauth2/refresh") {
                    parameter("grant_type", "refresh_token")
                    header("Authorization", authorization)
                    body = FormDataContent(Parameters.build {
                        append("refresh_token", refreshToken)
                    })
                }
                println(update)
                val newTokens: HueAuthResponse = JSON.nonstrict.parse(HueAuthResponse.serializer(), update)
                ssm.putParameter {
                    it.name("/External/Hue/drakoni/RefreshToken")
                    it.type(ParameterType.SECURE_STRING)
                    it.value(newTokens.refresh_token)
                    it.overwrite(true)
                }
                ssm.putParameter {
                    it.name("/External/Hue/drakoni/Token")
                    it.type(ParameterType.SECURE_STRING)
                    it.value(newTokens.access_token)
                    it.overwrite(true)
                }
            } catch (e: BadResponseStatusException) {
                println(e.response.content.readUTF8Line())
            }
        }
    }
}

@Serializable
data class HueAuthResponse(
        val access_token: String,
        val access_token_expires_in: String,
        val refresh_token: String,
        val refresh_token_expires_in: String,
        val token_type: String
)