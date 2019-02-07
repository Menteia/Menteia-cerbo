package xyz.trankvila.menteia

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import io.ktor.application.install
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import paroli
import xyz.trankvila.menteia.cerbo.Cerbo
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.nio.ByteBuffer

fun main() {
    FirebaseApp.initializeApp(FirebaseOptions.builder()
            .setCredentials(
                    GoogleCredentials.fromStream(
                            ByteArrayInputStream(System.getenv("FIREBASE_CONFIG").toByteArray())
                    )
            )
            .setDatabaseUrl("https://menteia.firebaseio.com")
            .build()
    )
    val servilo = embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 7777) {
        install(WebSockets)
        routing {
            webSocket("/") {
                println("Konektis")
                val sekvaMesaĝo = { mesaĝo: String ->
                    launch {
                        outgoing.send(Frame.Text(mesaĝo))
                        val parolado = paroli(mesaĝo)
                        outgoing.send(Frame.Binary(true, ByteBuffer.wrap(parolado)))
                    }
                }
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
                                        val elirarbo = Cerbo.trakti(teksto, sekvaMesaĝo)
                                        outgoing.send(Frame.Text(elirarbo.toString()))
                                        val parolado = paroli(elirarbo)
                                        outgoing.send(Frame.Binary(true, ByteBuffer.wrap(parolado)))
                                    } catch (e: Exception) {
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