package xyz.trankvila.menteia.datumo

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ssm.SsmClient
import software.amazon.awssdk.services.ssm.model.GetParameterRequest
import software.amazon.awssdk.services.ssm.model.GetParametersRequest

object Sekretoj {
    val ssm = SsmClient.builder()
            .region(Region.US_WEST_2)
            .build()

    val OpenWeatherMapKey: String
        get() {
            val respondo = ssm.getParameter(GetParameterRequest.builder()
                    .name("/External/OpenWeatherMap")
                    .withDecryption(true)
                    .build())
            return respondo.parameter().value()
        }

    fun NestKey(name: String): String {
        return ssm.getParameter(GetParameterRequest.builder()
                .name("/External/Nest/$name")
                .withDecryption(true)
                .build())
                .parameter().value()
    }

    fun NestDeviceID(name: String): String {
        return ssm.getParameter(GetParameterRequest.builder()
                .name("/External/Nest/$name/ID")
                .withDecryption(true)
                .build())
                .parameter().value()
    }

    fun NetatmoCredentials(): Map<String, String> {
        return ssm.getParameters(GetParametersRequest.builder()
                .names(listOf("ClientID", "ClientSecret", "Username", "Password").map {
                    "/External/Netatmo/$it"
                })
                .withDecryption(true)
                .build()).parameters().map {
            it.name() to it.value()
        }.toMap()
    }

    fun HueLightID(name: String): String {
        return ssm.getParameter(GetParameterRequest.builder()
                .name("/External/Hue/drakoni/$name")
                .withDecryption(true)
                .build())
                .parameter().value()
    }

    fun HueToken(): String {
        return ssm.getParameter(GetParameterRequest.builder()
                .name("/External/Hue/drakoni/Token")
                .withDecryption(true)
                .build())
                .parameter().value()
    }

    fun HueUsername(): String {
        return ssm.getParameter(GetParameterRequest.builder()
                .name("/External/Hue/drakoni/Username")
                .withDecryption(true)
                .build())
                .parameter().value()
    }

    fun TwilioCredentials(): Pair<String, String> {
        val valuoj = ssm.getParameters(GetParametersRequest.builder()
                .names(listOf("SID", "AuthToken").map {
                    "/External/Twilio/$it"
                })
                .withDecryption(true)
                .build()).parameters()
        return valuoj[0].value() to valuoj[1].value()
    }

    val HereCredentials: Pair<String, String>
        get() {
            val valuoj = ssm.getParameters(GetParametersRequest.builder()
                    .names(listOf("AppID", "AppCode").map {
                        "/External/Here/$it"
                    })
                    .withDecryption(true)
                    .build()).parameters()
            return valuoj[1].value() to valuoj[0].value()
        }

    // Necesa pro Netatmo
    val session = mutableMapOf<String, String>()
}