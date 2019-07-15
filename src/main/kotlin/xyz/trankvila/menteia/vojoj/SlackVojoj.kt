package xyz.trankvila.menteia.vojoj

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.header
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xyz.trankvila.menteia.Agordo
import xyz.trankvila.menteia.SlackKonversacio
import xyz.trankvila.menteia.datumo.*
import xyz.trankvila.menteia.logger
import xyz.trankvila.menteia.memoro.Konversacio
import xyz.trankvila.menteia.tipsistemo.MenteiaTipEkcepcio
import xyz.trankvila.menteia.tipsistemo.meli
import xyz.trankvila.menteia.tipsistemo.remis
import xyz.trankvila.menteia.tipsistemo.timis
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.reflect.KClass

fun Route.slackVojoj() {
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
                                        logger.debug("Ricevis mesaĝo: <${mesaĝoEvento.text}> de ${mesaĝoEvento.user}")
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
}