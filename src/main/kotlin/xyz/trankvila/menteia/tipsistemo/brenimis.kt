package xyz.trankvila.menteia.tipsistemo

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import kotlinx.coroutines.*
import kotlinx.io.ByteArrayInputStream
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.jackson
import xyz.trankvila.menteia.tipsistemo.interna._certeco
import xyz.trankvila.menteia.tipsistemo.interna._forigebla
import xyz.trankvila.menteia.tipsistemo.interna._kreebla
import xyz.trankvila.menteia.tipsistemo.interna._nomitaAĵo
import xyz.trankvila.menteia.transport
import java.io.FileInputStream
import java.time.*
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalField
import java.util.*

class brenimis(val nomo: String): timis(), _kreebla, _forigebla {
    companion object {
        val calendarID = "dsn2ij1hfhe9b8ob9qm58b397c@group.calendar.google.com"
        val credentials = GoogleCredential.fromStream(
                if (System.getenv("IS_HEROKU") != null) {
                    ByteArrayInputStream(System.getenv("GOOGLE_SERVICE_ACCOUNT").toByteArray())
                } else {
                    FileInputStream("Menteia-2193c8a41ecb.json")
                }
        ).createScoped(listOf(CalendarScopes.CALENDAR))
        val service = Calendar.Builder(
                transport,
                jackson,
                credentials
        ).setApplicationName("Menteia")
                .build()

        suspend fun _krei(nomo: String, opcioj: sadimis<_nomitaAĵo, divimis<karimis, ŝanamis>, teremis>): brenimis {
            val _nomo = opcioj._valuo1
            val tempo = ZonedDateTime.of(
                    LocalDateTime.of(opcioj._valuo2._valuo1._valuigi(), opcioj._valuo2._valuo2._valuigi()),
                    ZoneId.of("America/Toronto")
            )
            val fino = tempo.plus(opcioj._valuo3._valuigi())

            val evento = service.events().insert(calendarID,
                    Event().setSummary("$_nomo - $nomo")
                            .setStart(EventDateTime().setDateTime(DateTime(Date.from(tempo.toInstant()))))
                            .setEnd(EventDateTime().setDateTime(DateTime(Date.from(fino.toInstant()))))
            ).execute()
            alirilaro.kreiEventon(nomo, evento.id)
            return brenimis(nomo)
        }
    }

    override val _tipo = _certeco.negi

    override suspend fun _valuigi() = withContext(Dispatchers.Default) {
        val evento = service.events().get(calendarID, alirilaro.alportiEventon(nomo)).execute()
        evento
    }

    override suspend fun _simpligi(): sadika<_nomitaAĵo, divis<karima, ŝanamis>, teremis> {
        val evento = _valuigi()
        val nomo = evento.summary.split(" - ")[0]
        val komenco = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(evento.start.dateTime.value),
                ZoneId.of("America/Toronto")
        )
        val fino = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(evento.end.dateTime.value),
                ZoneId.of("America/Toronto")
        )
        val minutoj = komenco.until(fino, ChronoUnit.MINUTES)
        val daŭro = if (minutoj % 60 == 0L) {
            gomos(lemis.ciferigi((minutoj / 60).toBigInteger()))
        } else {
            nires(lemis.ciferigi(minutoj.toBigInteger()))
        }
        return sadika(_nomitaAĵo(nomo), divis(karima.igi(komenco.toLocalDate()), ŝanamis.igi(komenco.toLocalTime())), daŭro)
    }

    override suspend fun _forigi(): vanemis.tadumis {
        service.events().delete(calendarID, alirilaro.alportiEventon(nomo)).execute()
        alirilaro.forigiEventon(nomo)
        return klos(sindis(this))
    }

    suspend fun medisas(morem: ŝanamis): vanemis.tadumis {
        val evento = _valuigi()
        val dato = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(evento.start.dateTime.value),
                ZoneId.of("America/Toronto")
        )
        val fino = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(evento.end.dateTime.value),
                ZoneId.of("America/Toronto")
        )
        val horo = morem._valuigi()
        val novaKomenco = dato.withHour(horo.hour).withMinute(horo.minute)
        val diferenco = novaKomenco.until(dato, ChronoUnit.MINUTES)
        val novaFino = fino.minusMinutes(diferenco)
        evento.start = EventDateTime().setDateTime(DateTime(novaKomenco.toInstant().toEpochMilli()))
        evento.end = EventDateTime().setDateTime(DateTime(novaFino.toInstant().toEpochMilli()))
        service.events().update(calendarID, evento.id, evento).execute()
        return to(this, _simpligi())
    }

    override fun toString(): String {
        return nomo
    }

    override fun traversi(): Sequence<String> {
        return sequence {
            yield(nomo)
        }
    }
}

class brema(morem: karimis): timis(morem) {
    override val _tipo = _certeco.negi
    val dato = runBlocking {  morem._valuigi() }

    override suspend fun _valuigi(): List<Event> {
        val eventoj = brenimis.service.events().list(brenimis.calendarID)
                .setTimeMin(DateTime(ZonedDateTime.of(dato, LocalTime.MIDNIGHT, ZoneId.of("America/Toronto")).toInstant().toEpochMilli()))
                .setTimeMax(DateTime(ZonedDateTime.of(dato.plusDays(1), LocalTime.MIDNIGHT, ZoneId.of("America/Toronto")).toInstant().toEpochMilli()))
                .execute()
        return eventoj.items
    }

    override suspend fun _simpligi(): brodimis<divimis<_nomitaAĵo, ŝanamis>> {
        val eventoj = _valuigi()
        return brotas.igiListon(eventoj.map {
            divis(_nomitaAĵo(it.summary.split(" - ")[0]), ŝanamis.igi(ZonedDateTime.ofInstant(Instant.ofEpochMilli(it.start.dateTime.value), ZoneId.of("America/Toronto")).toLocalTime()))
        })
    }
}