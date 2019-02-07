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

    // Necesa pro Netatmo
    val session = mutableMapOf<String, String>()
}