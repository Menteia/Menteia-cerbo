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
import xyz.trankvila.menteia.transport
import java.io.FileInputStream
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*

class brenimis(val nomo: String): timis(), _kreebla, _forigebla {
    companion object {
        val calendarID = "dsn2ij1hfhe9b8ob9qm58b397c@group.calendar.google.com"
        val credentials = GoogleCredential.fromStream(
                ByteArrayInputStream(System.getenv("GOOGLE_SERVICE_ACCOUNT").toByteArray())
//                FileInputStream("Menteia-2193c8a41ecb.json")
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
        val comenco = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(evento.start.dateTime.value),
                ZoneId.of("America/Toronto")
        )
        val fino = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(evento.end.dateTime.value),
                ZoneId.of("America/Toronto")
        )
        val minutoj = comenco.until(fino, ChronoUnit.MINUTES)
        val daŭro = if (minutoj % 60 == 0L) {
            gomos(lemis.ciferigi((minutoj / 60).toBigInteger()))
        } else {
            nires(lemis.ciferigi(minutoj.toBigInteger()))
        }
        return sadika(_nomitaAĵo(nomo), divis(karima.igi(comenco.toLocalDate()), ŝanamis.igi(comenco.toLocalTime())), daŭro)
    }

    suspend fun _forigi() {
        service.events().delete(calendarID, alirilaro.alportiEventon(nomo)).execute()
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