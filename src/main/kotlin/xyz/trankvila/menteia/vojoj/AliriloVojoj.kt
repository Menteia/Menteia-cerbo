package xyz.trankvila.menteia.vojoj

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.response.respondBytes
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.error
import kreiXML
import paroladoj
import paroli
import xyz.trankvila.menteia.datumo.AliriloRespondoAdapter
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.idKontrolo
import xyz.trankvila.menteia.logger
import xyz.trankvila.menteia.tipsistemo.*
import xyz.trankvila.menteia.vorttrakto.Legilo.legi

data class AliriloRespondo(
        val respondo: String,
        val ssml: String
)

fun Route.aliriloVojoj() {
    post("alirilo") {
        val xtoken = call.parameters["token"]
        if (xtoken == null || idKontrolo(xtoken) == null) {
            call.respond(HttpStatusCode.Unauthorized)
        } else {
            try {
                val respondo = legi(call.receiveText())._valuigi()
                if (respondo is timis) {
                    call.respondText(AliriloRespondoAdapter.toJson(AliriloRespondo(
                            respondo.toString(),
                            kreiXML(respondo)
                    )), ContentType.Application.Json)
                } else {
                    call.respondText(AliriloRespondoAdapter.toJson(AliriloRespondo(
                            respondo?.toString() ?: "",
                            ""
                    )), ContentType.Application.Json, HttpStatusCode.BadRequest)
                }
            } catch (e: MenteiaTipEkcepcio) {
                call.respondText(AliriloRespondoAdapter.toJson(AliriloRespondo(
                        e._mesaĝo.toString(),
                        kreiXML(e._mesaĝo)
                )), ContentType.Application.Json)
            } catch (e: Exception) {
                logger.error(e)
                call.respondText(AliriloRespondoAdapter.toJson(AliriloRespondo(
                        e.message ?: "",
                        ""
                )), ContentType.Application.Json)
            }
        }
    }
    post("xml") {
        val xtoken = call.parameters["token"]
        if (xtoken == null || idKontrolo(xtoken) == null) {
            call.respond(HttpStatusCode.Unauthorized)
        } else {
            try {
                val demando = legi(call.receiveText())
                val ssml = kreiXML(demando, true)
                call.respondText(ssml)
            } catch (e: Exception) {
                logger.error(e)
                call.respondText(e.message ?: "", status = HttpStatusCode.BadRequest)
            }
        }
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