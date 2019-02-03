package xyz.`5atm`.menteia

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
import paroli
import xyz.`5atm`.menteia.cerbo.Cerbo
import java.nio.ByteBuffer

fun main() {
    val servilo = embeddedServer(Netty, port = 7777) {
        install(WebSockets)
        routing {
            webSocket("/") {
                while (true) {
                    val frame = incoming.receive()
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
                }
            }
        }
    }
    servilo.start(wait = true)
}