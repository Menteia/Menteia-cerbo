package xyz.trankvila.menteia.datumo

import awaitStringResponse
import com.github.kittinunf.fuel.Fuel
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.BadResponseStatusException
import io.ktor.client.request.*
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.serialization.json.JSON
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*
import software.amazon.awssdk.services.lambda.LambdaClient
import software.amazon.awssdk.services.lambda.model.InvokeRequest
import java.lang.Exception
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URL
import java.util.*

object RealaAlirilaro : Alirilaro {
    private val db = DynamoDbClient.builder()
            .region(Region.US_WEST_2)
            .build()
    private val lambda = LambdaClient.builder()
            .region(Region.US_EAST_1)
            .build()

    override fun alportiListon(nomo: String): List<String> {
        val respondo = db.query(QueryRequest.builder()
                .tableName("Menteia-datumejo")
                .expressionAttributeValues(mapOf(":nomo" to AttributeValue.builder().s(nomo).build()))
                .keyConditionExpression("vorto = :nomo")
                .build()
        )
        if (respondo.count() != 1) {
            throw Exception("Ne eblis trovi la liston nomita ${nomo}")
        }
        return respondo.items()[0]["enhavo"]!!.l().map {
            it.s()
        }
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
                                "tipo" to AttributeValue.builder().s(tipo).build(),
                                "valenco" to AttributeValue.builder().n("0").build(),
                                "aktantoj" to AttributeValue.builder().l(listOf()).build()
                        ))
                        .build())
                Vortaro.alporti(alporti = true)
                return vorto
            } catch (e: ConditionalCheckFailedException) {
                continue
            }
        }
    }

    override fun kreiListon(tipo: String): String {
        val nomo = kreiNomon("brodimis $tipo")
        db.updateItem(UpdateItemRequest.builder()
                .tableName("Menteia-datumejo")
                .key(mapOf(
                        "vorto" to AttributeValue.builder().s(nomo).build()
                ))
                .updateExpression("set enhavo = :e")
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
                .conditionExpression("begins_with(tipo, :t)")
                .expressionAttributeValues(mapOf(
                        ":t" to AttributeValue.builder().s("samona").build()
                ))
                .key(mapOf(
                        "vorto" to AttributeValue.builder().s(nomo).build()
                ))
                .build()
        )
        Vortaro.alporti(alporti = true)
    }

    override suspend fun getThermostatState(id: String): ThermostatState {
        HttpClient(Apache).use {
            val respondo = it.get<String>(
                    URL("https://developer-api.nest.com/devices/thermostats/${Sekretoj.NestDeviceID(id)}")
            ) {
                headers {
                    append("Authorization", "Bearer ${Sekretoj.NestKey(id)}")
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

    override suspend fun setThermostatMode(id: String, mode: String, t1: BigDecimal?, t2: BigDecimal?) {
        val modeLabel = hvacModesReversed.getValue(mode)
        val additionalBodyContent = when (modeLabel) {
            "heat", "cool" -> "{\"target_temperature_c\": ${t1!!.toPlainString()}}"
            "heat-cool" -> "{\"target_temperature_low_c\": ${t1!!.toPlainString()}, \"target_temperature_high_c\": ${t2!!.toPlainString()}}"
            else -> null
        }
        HttpClient(Apache).use {
            try {
                it.put<String>(URL("https://developer-api.nest.com/devices/thermostats/${Sekretoj.NestDeviceID(id)}")) {
                    headers {
                        append("Authorization", "Bearer ${Sekretoj.NestKey(id)}")
                    }
                    body = TextContent("{\"hvac_mode\": \"$modeLabel\"}", ContentType.Application.Json)
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

    override suspend fun getCurrentWeather(location: Int): WeatherReport {
        val (_, _, result) = Fuel.get(
                "https://api.openweathermap.org/data/2.5/weather",
                listOf("id" to location, "appid" to Sekretoj.OpenWeatherMapKey, "units" to "metric")
        ).awaitStringResponse()
        return result.fold(
                { data -> JSON.nonstrict.parse(WeatherReport.serializer(), data) },
                { error -> throw error.exception }
        )
    }

    override suspend fun getForecast(location: Int, date: Calendar): WeatherReport? {
        val (_, _, result) = Fuel.get(
                "https://api.openweathermap.org/data/2.5/forecast",
                listOf("id" to location, "appid" to Sekretoj.OpenWeatherMapKey, "units" to "metric")
        ).awaitStringResponse()
        val ts = (date.timeInMillis + date.timeZone.getOffset(date.timeInMillis)) / 1000
        return result.fold(
                { data ->
                    val raporto: Raporto = JSON.nonstrict.parse(Raporto.serializer(), data)
                    raporto.list.forEach {
                        if (it.dt == ts) {
                            return it
                        }
                    }
                    null
                }, { error -> throw error.exception }
        )
    }

    override suspend fun getLightBulbState(name: String): LightBulbState {
        HttpClient(Apache).use {
            val response = it.get<String>("https://api.meethue.com/bridge/${Sekretoj.HueUsername()}/lights/${
                Sekretoj.HueLightID(name)
            }") {
                headers {
                    append("Authorization", "Bearer ${Sekretoj.HueToken()}")
                }
            }
            return JSON.nonstrict.parse(HueResponse.serializer(), response).state
        }
    }

    override suspend fun setLightBulbOn(name: String, on: Boolean) {
        HttpClient(Apache).use {
            it.put<String>("https://api.meethue.com/bridge/${Sekretoj.HueUsername()}/lights/${
                Sekretoj.HueLightID(name)
            }/state") {
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
}