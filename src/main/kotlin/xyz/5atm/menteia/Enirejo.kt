package xyz.`5atm`.menteia

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
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
import paroli
import xyz.`5atm`.menteia.cerbo.Cerbo
import java.io.FileInputStream
import java.nio.ByteBuffer

fun main() {
    FirebaseApp.initializeApp(FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(FileInputStream("menteia-firebase-adminsdk-7y32e-149f25a3cb.json")))
            .setDatabaseUrl("https://menteia.firebaseio.com")
            .build()
    )
    val servilo = embeddedServer(Netty, port = 7777) {
        install(WebSockets)
        routing {
            webSocket("/") {
                println("?!")
                var token = idRicevo(incoming, outgoing)
                while (true) {
                    val frame = incoming.receive()
                    if (idKontrolo(token)) {
                        when (frame) {
                            is Frame.Text -> {
                                val teksto = frame.readText()
                                println("Eniro: $teksto")
                                if (teksto.equals("fino")) {
                                    close(CloseReason(CloseReason.Codes.NORMAL, "Fino"))
                                } else {
                                    try {
                                        val elirarbo = Cerbo.trakti(teksto)
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