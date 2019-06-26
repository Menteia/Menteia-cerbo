package xyz.trankvila.menteia.datumo

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.BadResponseStatusException
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import kotlinx.coroutines.io.jvm.javaio.toInputStream
import kotlinx.coroutines.io.readUTF8Line
import org.slf4j.LoggerFactory

data class AccessTokenResponse(
        val access_token: String,
        val scope: String,
        val token_type: String,
        val expires_in: Int
)

data class ADMMessage(
        val text: String,
        val paroloID: String? = null
)

data class ADMRequest(
        val data: ADMMessage
)

suspend fun getAccessToken() {
    HttpClient(Apache).use {
        val id = Sekretoj.ADMClientID.await()
        val secret = Sekretoj.ADMClientSecret.await()
        val response = it.post<String>("https://api.amazon.com/auth/O2/token") {
            body = FormDataContent(Parameters.build {
                append("grant_type", "client_credentials")
                append("scope", "messaging:push")
                append("client_id", id)
                append("client_secret", secret)
            })
        }
        val parsed = ADMAccessTokenResponseAdapter.fromJson(response)!!
        Sekretoj.admtoken = parsed.access_token
    }
}

suspend fun sendADMMessage(message: ADMMessage, destination: String) {
    HttpClient(Apache).use {
        try {
            it.post<String>("https://api.amazon.com/messaging/registrations/$destination/messages") {
                header("Authorization", "Bearer ${Sekretoj.admtoken}")
                header("X-Amzn-Type-Version", "com.amazon.device.messaging.ADMMessage@1.0")
                header("Accept", "application/json")
                header("X-Amzn-Accept-Type", "com.amazon.device.messaging.ADMSendResult@1.0")
                body = TextContent(
                        ADMRequestAdapter.toJson(ADMRequest(message)),
                        ContentType.Application.Json
                )
            }
        } catch (e: BadResponseStatusException) {
            val content = e.response.content
            var line = content.readUTF8Line()
            while (line != null) {
                logger.error(line)
                line = content.readUTF8Line()
            }
        }
    }
}