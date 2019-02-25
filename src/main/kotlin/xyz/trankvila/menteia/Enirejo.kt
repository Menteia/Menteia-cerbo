package xyz.trankvila.menteia

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Notification
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.request.receiveParameters
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondBytes
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.io.ByteArrayInputStream
import kotlinx.serialization.*
import kotlinx.serialization.json.JSON
import paroladoj
import paroli
import xyz.trankvila.menteia.datumo.RealaAlirilaro
import xyz.trankvila.menteia.datumo.Sekretoj
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.tipsistemo.MenteiaTipEkcepcio
import xyz.trankvila.menteia.tipsistemo.timis
import xyz.trankvila.menteia.vorttrakto.Legilo
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.scheduleAtFixedRate

@Serializable
data class Respondo(val teksto: String, val UUID: String)

fun main() {
    val ids = mutableMapOf<String, String>()
    Timer().scheduleAtFixedRate(3600000 * 24, 3600000 * 24) {
        GlobalScope.launch {
            println("Updating Hue tokens")
            RealaAlirilaro.refreshHueToken()
        }
    }

    FirebaseApp.initializeApp(FirebaseOptions.builder()
            .setCredentials(
//                    GoogleCredentials.fromStream(
//                            FileInputStream("menteia-firebase-adminsdk-7y32e-149f25a3cb.json")
//                    )
                    GoogleCredentials.fromStream(
                            ByteArrayInputStream(System.getenv("FIREBASE_CONFIG").toByteArray())
                    )
            )
            .setDatabaseUrl("https://menteia.firebaseio.com")
            .build()
    )
    val (sid, token) = Sekretoj.TwilioCredentials()
    Twilio.init(sid, token)
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
                            Agordo.sendiMesaĝon.set {
                                val paroloID = paroli(it)
                                sendiMesaĝon(it.toString(), ids.getValue(uid), paroloID.toString())
                            }
                            try {
                                val respondo = Legilo.legi(peto)._valuigi()
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
                        println("Parolas $id")
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
                        println("Konektis: $firebaseDeviceID")
                        call.respond(HttpStatusCode.NoContent)
                    }
                }
            }
            post("/sms") {
                val peto = call.receiveParameters()
                val enhavo = peto["Body"]!!
                Agordo.sendiMesaĝon.set {
                    println("Sendas mesaĝo al ${peto["From"]}")
                    val mesaĝo = Message.creator(
                            PhoneNumber(peto["From"]!!),
                            PhoneNumber("+15206368342"),
                            it.toString()
                    ).create()
                    println("Sendis ${mesaĝo.sid}")
                }
                try {
                    val arbo = Legilo.legi(enhavo)
                    call.respondText(arbo._valuigi()!!.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respondText("veguna")
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
    println("Sendis: $rezulto")
}