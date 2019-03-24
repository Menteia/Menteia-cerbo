package xyz.trankvila.menteia.datumo

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.await
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ssm.SsmAsyncClient
import software.amazon.awssdk.services.ssm.SsmClient
import software.amazon.awssdk.services.ssm.model.GetParameterRequest
import software.amazon.awssdk.services.ssm.model.GetParametersRequest

object Sekretoj {
    val ssm = SsmAsyncClient.builder()
            .region(Region.US_WEST_2)
            .build()

    val OpenWeatherMapKey: Deferred<String> get() {
        return GlobalScope.async {
            val respondo = ssm.getParameter(GetParameterRequest.builder()
                    .name("/External/OpenWeatherMap")
                    .withDecryption(true)
                    .build()).await()
            respondo.parameter().value()
        }
    }

    suspend fun NestKey(name: String): String {
        return ssm.getParameter(GetParameterRequest.builder()
                .name("/External/Nest/$name")
                .withDecryption(true)
                .build())
                .await()
                .parameter().value()
    }

    suspend fun NestDeviceID(name: String): String {
        return ssm.getParameter(GetParameterRequest.builder()
                .name("/External/Nest/$name/ID")
                .withDecryption(true)
                .build())
                .await()
                .parameter().value()
    }

    suspend fun NetatmoCredentials(): Map<String, String> {
        return ssm.getParameters(GetParametersRequest.builder()
                .names(listOf("ClientID", "ClientSecret", "Username", "Password").map {
                    "/External/Netatmo/$it"
                })
                .withDecryption(true)
                .build()).await().parameters().map {
            it.name() to it.value()
        }.toMap()
    }

    suspend fun HueLightID(name: String): String {
        return ssm.getParameter(GetParameterRequest.builder()
                .name("/External/Hue/drakoni/$name")
                .withDecryption(true)
                .build())
                .await()
                .parameter().value()
    }

    suspend fun HueToken(): String {
        return ssm.getParameter(GetParameterRequest.builder()
                .name("/External/Hue/drakoni/Token")
                .withDecryption(true)
                .build())
                .await()
                .parameter().value()
    }

    suspend fun HueUsername(): String {
        return ssm.getParameter(GetParameterRequest.builder()
                .name("/External/Hue/drakoni/Username")
                .withDecryption(true)
                .build())
                .await()
                .parameter().value()
    }

    suspend fun TwilioCredentials(): Pair<String, String> {
        val valuoj = ssm.getParameters(GetParametersRequest.builder()
                .names(listOf("SID", "AuthToken").map {
                    "/External/Twilio/$it"
                })
                .withDecryption(true)
                .build()).await().parameters()
        return valuoj[1].value() to valuoj[0].value()
    }

    val HereCredentials: Deferred<Pair<String, String>> get() {
        return GlobalScope.async {
            val valuoj = ssm.getParameters(GetParametersRequest.builder()
                    .names(listOf("AppID", "AppCode").map {
                        "/External/Here/$it"
                    })
                    .withDecryption(true)
                    .build()).await().parameters()
            valuoj[1].value() to valuoj[0].value()
        }
    }

    var ssk: String? = null

    val SlackSigningKey: Deferred<String> get() {
        return GlobalScope.async {
            if (ssk == null) {
                ssk = ssm.getParameter {
                    it.name("/External/Slack/SigningSecret")
                    it.withDecryption(true)
                }.await().parameter().value()
            }
            ssk!!
        }
    }

    var sbk: String? = null

    val SlackBotKey: Deferred<String> get() = GlobalScope.async {
        if (sbk == null) {
            sbk = ssm.getParameter {
                it.name("/External/Slack/BotKey")
                it.withDecryption(true)
            }.await().parameter().value()
        }
        sbk!!
    }

    // Necesa pro Netatmo
    val session = mutableMapOf<String, String>()
}