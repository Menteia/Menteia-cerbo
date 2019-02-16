package xyz.trankvila.menteia

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.twilio.Twilio
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
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.io.ByteArrayInputStream
import kotlinx.serialization.*
import kotlinx.serialization.json.JSON
import paroli
import xyz.trankvila.menteia.cerbo.Cerbo
import xyz.trankvila.menteia.datumo.Sekretoj
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.tipsistemo.MenteiaTipEkcepcio
import xyz.trankvila.menteia.tipsistemo.timis
import xyz.trankvila.menteia.vorttrakto.Legilo
import java.io.FileInputStream
import java.nio.ByteBuffer

fun main() {
    FirebaseApp.initializeApp(FirebaseOptions.builder()
            .setCredentials(
                    GoogleCredentials.fromStream(
                            FileInputStream("menteia-firebase-adminsdk-7y32e-149f25a3cb.json")
                    )
//                    GoogleCredentials.fromStream(
//                            ByteArrayInputStream(System.getenv("FIREBASE_CONFIG").toByteArray())
//                    )
            )
            .setDatabaseUrl("https://menteia.firebaseio.com")
            .build()
    )
    val (sid, token) = Sekretoj.TwilioCredentials()
    Twilio.init(sid, token)
    val servilo = embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 7777) {
        install(WebSockets)
        routing {
            webSocket("/") {
                println("Konektis")
                var token = idRicevo(incoming, outgoing)
                while (true) {
                    val frame = incoming.receive()
                    if (idKontrolo(token)) {
                        when (frame) {
                            is Frame.Text -> {
                                val teksto = frame.readText()
                                if (teksto.equals("fino")) {
                                    close(CloseReason(CloseReason.Codes.NORMAL, "Fino"))
                                } else {
                                    try {
                                        val elirarbo = Legilo.legi(teksto)._valuigi()
                                        if (elirarbo !is timis) {
                                            throw Exception("kalkulis: $elirarbo")
                                        }
                                        outgoing.send(Frame.Text(elirarbo.toString()))
                                        val parolado = paroli(elirarbo)
                                        outgoing.send(Frame.Binary(true, ByteBuffer.wrap(parolado)))
                                    } catch (e: MenteiaTipEkcepcio) {
                                        outgoing.send(Frame.Text(e._mesaĝo.toString()))
                                        val parolado = paroli(e._mesaĝo)
                                        outgoing.send(Frame.Binary(true, ByteBuffer.wrap(parolado)))
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        outgoing.send(Frame.Text("veguna (${e.message})"))
                                    }
                                }
                            }
                        }
                    } else {
                        token = idRicevo(incoming, outgoing)
                    }
                }
            }
            post("/sms") {
                val peto = call.receiveParameters()
                val enhavo = peto["Body"]!!
                val sekvaMesaĝo = { mesaĝo: String ->
                    launch {
                        val smsMesaĝo = com.twilio.rest.api.v2010.account.Message.creator(
                                PhoneNumber(peto["From"]!!),
                                PhoneNumber("+15206368342"),
                                mesaĝo
                        ).create()
                        println("Mesaĝo: ${smsMesaĝo.sid}")
                    }
                }
                try {
                    val respondo = Cerbo.trakti(enhavo, sekvaMesaĝo)
                    call.respondText(respondo.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respondText("veguna")
                }
            }
            get("/brodimis") {
                val idToken = call.parameters["token"]
                if (idToken == null || !idKontrolo(idToken)) {
                    call.respond(HttpStatusCode.Unauthorized)
                } else {
                    val listoj = alirilaro.ĉiujListoj()
                    call.respondText(JSON.stringify(
                            (String.serializer() to String.serializer().list).map,
                            listoj
                    ), ContentType.Application.Json);
                }
            }
        }
    }
    servilo.start(wait = true)
}

suspend fun idRicevo(incoming: ReceiveChannel<Frame>, outgoing: SendChannel<Frame>): String {
    while (true) {
        val idToken = incoming.receive()
        when (idToken) {
            is Frame.Text -> {
                val token = idToken.readText()
                if (idKontrolo(token)) {
                    outgoing.send(Frame.Text("!"))
                    return token
                } else {
                    outgoing.send(Frame.Text("?"))
                }
            }
        }
    }
}

fun idKontrolo(idToken: String): Boolean {
    try {
        FirebaseAuth.getInstance().verifyIdToken(idToken)
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}