package xyz.trankvila.menteia.datumo

import com.squareup.moshi.FromJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import kotlinx.serialization.Optional
import paroli
import xyz.trankvila.menteia.ids
import xyz.trankvila.menteia.sendiMesaĝon
import xyz.trankvila.menteia.tipsistemo.timis
import java.util.*

open class SlackEvent(
        open val type: String,
        val event: Any? = null
)

data class MessageEvent(
        val channel: String,
        val user: String? = null,
        val text: String,
        val ts: String,
        val event_ts: String,
        val channel_type: String,
        override val type: String
): SlackEvent(type)

data class SlackChallenge(
        val token: String,
        val challenge: String,
        override val type: String
): SlackEvent(type)

data class SlackMessage(
        val text: String,
        val channel: String
)

suspend fun sendMessage(user: String, message: timis) {
    val id = paroli(message)
    ids.forEach { _, u ->
        sendiMesaĝon(id.toString(), u, id.toString())
    }
    sendMessage(user, message.toString())
}

suspend fun sendMessage(user: String, message: String) {
    HttpClient(Apache).use {
        val key = "Bearer ${Sekretoj.SlackBotKey.await()}"
        it.post<String>("https://slack.com/api/chat.postMessage") {
            headers {
                append("Authorization", key)
                body = TextContent(
                        SlackMessageAdapter.toJson(SlackMessage(message, user)),
                        ContentType.Application.Json
                )
            }
        }
    }
}