package xyz.trankvila.menteia.vojoj

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JSON
import paroli
import xyz.trankvila.menteia.*
import xyz.trankvila.menteia.memoro.Konversacio
import xyz.trankvila.menteia.tipsistemo.MenteiaTipEkcepcio
import xyz.trankvila.menteia.tipsistemo.timis

fun Route.respondiVojoj() {
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
}