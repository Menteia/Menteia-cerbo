package xyz.trankvila.menteia.cerbo

import xyz.trankvila.menteia.cerbo.kiramis.Nombroj
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo

internal interface Lumo : NomitaAĵo {
    companion object {
        fun buvi(nomo: SintaksoArbo): Pair<String, Certeco> {
            TODO()
        }

        fun mavi(nomo: SintaksoArbo): Pair<String, Certeco> {
            TODO()
        }
    }

    override suspend fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "testos" -> testos()
            else -> super.invoke(eco)
        }
    }

    override suspend fun invoke(eco: SintaksoArbo, valuo: SintaksoArbo): Pair<String, Certeco> {
        TODO()
    }

    private fun testos(): Pair<String, Certeco> {
        TODO()
    }
}

internal object minero : Lumo {
    override val nomo = "minero"
}

internal object namida : Lumo {
    override val nomo = "namida"
}