package xyz.`5atm`.menteia.datumo

import awaitStringResponse
import com.github.kittinunf.fuel.Fuel
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.BadResponseStatusException
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.put
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
                .filterExpression("tipo = :nomo")
                .expressionAttributeValues(mapOf(
                        ":nomo" to AttributeValue.builder().s("girisa").build()
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
                .filterExpression("tipo = :nomo")
                .expressionAttributeValues(mapOf(
                        ":nomo" to AttributeValue.builder().s("girisa").build()
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

    override fun kreiNomon(): String {
        val respondo = lambda.invoke(InvokeRequest.builder()
                .functionName("Menteia-vortilo")
                .build())
        val vorto = respondo.payload().asUtf8String()
        return vorto.substring(1..vorto.length-2)
    }

    override fun kreiListon(): String {
        while (true) {
            val nomo = kreiNomon()
            try {
                db.putItem(PutItemRequest.builder()
                        .tableName("Menteia-datumejo")
                        .conditionExpression("attribute_not_exists(vorto)")
                        .item(mapOf(
                                "vorto" to AttributeValue.builder().s(nomo).build(),
                                "tipo" to AttributeValue.builder().s("girisa").build(),
                                "enhavo" to AttributeValue.builder().l(listOf()).build(),
                                "valenco" to AttributeValue.builder().n("0").build()
                        ))
                        .build())
                Vortaro.alporti(alporti = true)
                return nomo
            } catch (e: ConditionalCheckFailedException) {
                continue
            }
        }
    }

    override fun forigiListon(nomo: String) {
        db.deleteItem(DeleteItemRequest.builder()
                .tableName("Menteia-datumejo")
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

    override suspend fun setThermostatTemperature(id: String, targetTemperature: Int) {
        HttpClient(Apache).use {
            it.put<String>(URL("https://developer-api.nest.com/devices/thermostats/${Sekretoj.NestDeviceID(id)}")) {
                headers {
                    append("Authorization", "Bearer ${Sekretoj.NestKey(id)}")
                }
                body = TextContent("{\"target_temperature_c\": $targetTemperature}", ContentType.Application.Json)
            }
        }
    }

    override suspend fun setThermostatMode(id: String, mode: String, t1: Int?, t2: Int?) {
        val modeLabel = hvacModesReversed.getValue(mode)
        val additionalBodyContent = when (modeLabel) {
            "heat", "cool" -> "{\"target_temperature_c\": ${t1!!}}"
            "heat-cool" -> "{\"target_temperature_low_c\": ${t1!!}, \"target_temperature_high_c\": ${t2!!}}"
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
}