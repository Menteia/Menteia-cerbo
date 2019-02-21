package xyz.trankvila.menteia.cerbo

import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.kiramis.Nombroj
import xyz.trankvila.menteia.datumo.*
import kotlin.math.roundToInt

internal interface Termostato : NomitaAĵo {
    override suspend operator fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "testos" -> testos()
            "gremina" -> gremina()
            else -> super.invoke(eco)
        }
    }

    override suspend operator fun invoke(eco: SintaksoArbo, valuo: SintaksoArbo): Pair<String, Certeco> {
        TODO()
    }

    suspend fun testos(): Pair<String, Certeco> {
        TODO()
    }

    suspend fun gremina(): Pair<String, Certeco> {
        TODO()
    }
}

// Waterloo thermostat
internal object klisemi : Termostato {
    override val nomo = "klisemi"
}

internal object brinemi : Termostato {
    override val nomo = "brinemi"
}