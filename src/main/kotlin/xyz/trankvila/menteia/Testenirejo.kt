package xyz.trankvila.menteia

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.AclRule
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import xyz.trankvila.menteia.datumo.RealaAlirilaro
import xyz.trankvila.menteia.datumo.Sekretoj
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.memoro.Konversacio
import xyz.trankvila.menteia.tipsistemo.*
import xyz.trankvila.menteia.vorttrakto.Legilo
import java.io.FileInputStream
import java.time.LocalDate
import java.time.ZonedDateTime
import kotlin.reflect.KParameter

fun main() = runBlocking {
    val konversacio = Konversacio()
    launch {
        "doni ko lurina sitana".splitToSequence(" ").forEach {
            konversacio.eniri(it)
        }
    }
    println("...")
    println(konversacio.fini()._valuigi().toString())
}