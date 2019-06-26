package xyz.trankvila.menteia

import ch.qos.logback.classic.Level
import com.google.api.client.auth.oauth2.StoredCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.Json
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.DataStore
import com.google.api.client.util.store.DataStoreFactory
import com.google.api.client.util.store.MemoryDataStoreFactory
import com.google.api.services.calendar.CalendarScopes
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.MemoryTokensStorage
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Notification
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.origin
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.request.header
import io.ktor.request.receiveParameters
import io.ktor.request.receiveText
import io.ktor.response.header
import io.ktor.response.respond
import io.ktor.response.respondBytes
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.url
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.io.ByteArrayInputStream
import kotlinx.serialization.*
import kotlinx.serialization.json.JSON
import kotlinx.serialization.json.JsonObject
import org.slf4j.LoggerFactory
import paroladoj
import paroli
import xyz.trankvila.menteia.datumo.*
import xyz.trankvila.menteia.memoro.Konversacio
import xyz.trankvila.menteia.memoro.lokajObjektoj
import xyz.trankvila.menteia.tipsistemo.*
import xyz.trankvila.menteia.vorttrakto.Legilo
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.security.MessageDigest
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.logging.Logger
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.reflect.KClass

@Serializable
data class Respondo(val teksto: String, val UUID: String)

val ids = mutableMapOf<String, String>()
internal val logger = LoggerFactory.getLogger("Enirejo")

fun main() {
    Timer().scheduleAtFixedRate(3600000 * 24, 3600000 * 24) {
        GlobalScope.launch {
            RealaAlirilaro.refreshHueToken()
        }
    }
    Timer().scheduleAtFixedRate(3600000 * 3, 3600000 * 3) {
        GlobalScope.launch {
            authenticate()
        }
    }

    FirebaseApp.initializeApp(FirebaseOptions.builder()
            .setCredentials(
                    if (System.getenv("IS_HEROKU") != null) {
                        GoogleCredentials.fromStream(
                                ByteArrayInputStream(System.getenv("FIREBASE_CONFIG").toByteArray())
                        )
                    } else {
                        GoogleCredentials.fromStream(
                                FileInputStream("menteia-firebase-adminsdk-7y32e-149f25a3cb.json")
                        )
                    }
            )
            .setDatabaseUrl("https://menteia.firebaseio.com")
            .build()
    )
    val (sid, token) = runBlocking { Sekretoj.TwilioCredentials() }
    Twilio.init(sid, token)
    runBlocking { getAccessToken() }
    val servilo = embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 7777) {
        install(WebSockets)
        routing {
            post("/respondi") {
                val xtoken = call.parameters["token"]
                if (xtoken == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                } else {
                    val uid = idKontrolo(xtoken)
                    if (uid == null || !ids.containsKey(uid)) {
                        call.respond(HttpStatusCode.Unauthorized)
                    } else {
                        try {
                            val peto = call.receiveText()
                            Agordo.sendiMesaĝon = {
                                GlobalScope.launch {
                                    val paroloID = paroli(it)
                                    sendiMesaĝon(it.toString(), ids.getValue(uid), paroloID.toString())
                                }
                            }
                            try {
                                val konversacio = Konversacio()
                                peto.splitToSequence(" ").forEach {
                                    konversacio.eniri(it)
                                }
                                val respondo = konversacio.fini()._valuigi()
                                if (respondo !is timis) {
                                    throw Exception("kalkulis: $respondo")
                                } else {
                                    val paroloID = paroli(respondo)
                                    call.respondText(JSON.stringify(Respondo.serializer(), Respondo(respondo.toString(), paroloID.toString())),
                                            ContentType.Application.Json)
                                }
                            } catch (e: MenteiaTipEkcepcio) {
                                val paroloID = paroli(e._mesaĝo)
                                call.respondText(JSON.stringify(Respondo.serializer(), Respondo(e._mesaĝo.toString(), paroloID.toString())),
                                        ContentType.Application.Json)
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            call.respondText(
                                    "vaguna (${e.message})",
                                    status = HttpStatusCode.BadRequest
                            )
                        }
                    }
                }
            }
            get("/paroli") {
                val xtoken = call.parameters["token"]
                val id = call.parameters["id"]
                if (xtoken == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                } else if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    val uuid = UUID.fromString(id)
                    val parolado = paroladoj.remove(uuid)
                    if (parolado == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        logger.info("Parolas $id")
                        call.response.header("Access-Control-Allow-Origin", "http://localhost:3000")
                        call.respondBytes(parolado.await().enhavo, ContentType.Audio.OGG)
                    }
                }
            }
            post("/sciigi") {
                val xtoken = call.parameters["token"]
                if (xtoken == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                } else {
                    val uid = idKontrolo(xtoken)
                    if (uid == null) {
                        call.respond(HttpStatusCode.Unauthorized)
                    } else {
                        val firebaseDeviceID = call.receiveText()
                        ids[uid] = firebaseDeviceID
                        logger.debug("Konektis: $firebaseDeviceID")
                        call.respond(HttpStatusCode.NoContent)
                        sendADMMessage(ADMMessage("Test"), firebaseDeviceID)
                    }
                }
            }
            post("/sms") {
                val peto = call.receiveParameters()
                val enhavo = peto["Body"]!!
                Agordo.sendiMesaĝon = {
                    GlobalScope.launch {
                        Message.creator(
                                PhoneNumber(peto["From"]!!),
                                PhoneNumber("+15206368342"),
                                it.toString()
                        ).create()
                    }
                }
                try {
                    val arbo = Legilo.legi(enhavo)
                    call.respondText(arbo._valuigi()!!.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respondText("veguna")
                }
            }
            get("/sanimis") {
                val xtoken = call.parameters["token"]
                if (xtoken == null || idKontrolo(xtoken) == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                } else {
                    val tempoŝaltiloj = lokajObjektoj.filterValues {
                        it is sanimis
                    }.map {
                        val tempoŝaltilo = it.value as sanimis
                        _tempoŝaltilo(tempoŝaltilo._nomo, tempoŝaltilo.celo.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                    }
                    call.respondText(
                            JSON.stringify(_tempoŝaltilo.serializer().list, tempoŝaltiloj)
                    )
                }
            }
            post("/slack") {
                try {
                    val ts = call.request.header("X-Slack-Request-Timestamp")!!
                    val s = call.request.header("X-Slack-Signature")!!
                    val peto = call.receiveText()
                    val sig = "v0:$ts:$peto"
                    val key = SecretKeySpec(Sekretoj.SlackSigningKey.await().toByteArray(), "HmacSHA256")
                    val mac = Mac.getInstance("HmacSHA256")
                    mac.init(key)
                    val res = "v0=${mac.doFinal(sig.toByteArray()).joinToString("") {
                        "%02x".format(it)
                    }}"
                    if (res != s) {
                        throw java.lang.Exception("Slack signature verification failed")
                    }
                    val evento = SlackEventAdapter.fromJson(peto)!!
                    when (evento.type) {
                        "url_verification" -> {
                            val urlVerification = SlackChallengeAdatper.fromJson(peto)!!
                            call.respondText(urlVerification.challenge)
                        }
                        "event_callback" -> {
                            val realaEvento = SlackEventAdapter.fromJsonValue(evento.event)!!
                            when (realaEvento.type) {
                                "message" -> {
                                    val mesaĝoEvento = SlackMessageEventAdapter.fromJsonValue(evento.event)!!
                                    when (mesaĝoEvento.channel_type) {
                                        "im" -> {
                                            if (mesaĝoEvento.user != null) {
                                                launch {
                                                    Agordo.sendiMesaĝon = {
                                                        GlobalScope.launch {
                                                            logger.info("Sendas: $it")
                                                            sendMessage(mesaĝoEvento.channel, it)
                                                        }
                                                    }
                                                    try {
                                                        val bezonata = mesaĝoEvento.text.splitToSequence(" ")
                                                                .fold<String, KClass<out timis>?>(null) { _, s ->
                                                                    SlackKonversacio.eniri(s)
                                                                }
                                                        if (bezonata == null) {
                                                            val respondo = SlackKonversacio.fini()._valuigi()
                                                            if (respondo is timis) {
                                                                sendMessage(mesaĝoEvento.channel, respondo)
                                                            } else {
                                                                sendMessage(mesaĝoEvento.channel, respondo.toString())
                                                            }
                                                            SlackKonversacio = Konversacio()
                                                        } else {
                                                            sendMessage(mesaĝoEvento.channel, meli(remis(bezonata)))
                                                        }
                                                    } catch (e: MenteiaTipEkcepcio) {
                                                        sendMessage(mesaĝoEvento.channel, e._mesaĝo)
                                                        SlackKonversacio = Konversacio()
                                                    } catch (e: java.lang.Exception) {
                                                        e.printStackTrace()
                                                        sendMessage(mesaĝoEvento.channel, e.message ?: e::class.simpleName!!)
                                                        SlackKonversacio = Konversacio()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                call.respond(HttpStatusCode.OK)
            }
            route("alirilo") {
                get("now") {
                    val xtoken = call.parameters["token"]
                    if (xtoken == null || idKontrolo(xtoken) == null) {
                        call.respond(HttpStatusCode.Unauthorized)
                    } else {
                        val respondo = doni(ko(geradas()))._valuigi()
                        val id = paroli(respondo)
                        val parolado = paroladoj.remove(id)!!.await()
                        call.respondBytes(parolado.enhavo, ContentType.Audio.OGG)
                    }
                }
                get("today") {
                    val xtoken = call.parameters["token"]
                    if (xtoken == null || idKontrolo(xtoken) == null) {
                        call.respond(HttpStatusCode.Unauthorized)
                    } else {
                        val respondo = doni(ko(fidinas()))._valuigi()
                        val id = paroli(respondo)
                        val parolado = paroladoj.remove(id)!!.await()
                        call.respondBytes(parolado.enhavo, ContentType.Audio.OGG)
                    }
                }
                get("temperature") {
                    val xtoken = call.parameters["token"]
                    if (xtoken == null || idKontrolo(xtoken) == null) {
                        call.respond(HttpStatusCode.Unauthorized)
                    } else {
                        val respondo = alirilaro.getWeatherStationState()
                        call.respondText(respondo.body.devices[0].dashboard_data.Temperature.toString())
                    }
                }
            }
        }
    }
    servilo.start(wait = true)
}

fun idKontrolo(idToken: String): String? {
    try {
        val uzanto = FirebaseAuth.getInstance().verifyIdToken(idToken)
        return uzanto.uid
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun sendiMesaĝon(enhavo: String, al: String, paroloID: String? = null) {
    val rezulto = FirebaseMessaging.getInstance().send(
            com.google.firebase.messaging.Message.builder()
                    .setNotification(
                            Notification(
                                    "Respondo de Menteia cerbo",
                                    enhavo
                            )
                    )
                    .putData("paroloID", paroloID ?: "")
                    .setToken(al)
                    .build()
    )
    logger.info("Sendis: $rezulto")
}