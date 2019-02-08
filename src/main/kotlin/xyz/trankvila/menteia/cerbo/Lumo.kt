package xyz.trankvila.menteia.cerbo

import xyz.trankvila.menteia.cerbo.kiram.Nombroj
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo

internal interface Lumo : NomitaAĵo {
    companion object {
        suspend fun buvi(nomo: SintaksoArbo): Pair<String, Certeco> {
            alirilaro.setLightBulbOn(nomo.radiko, false)
            return "to des $nomo testos buve" to Certeco.Regi
        }

        suspend fun mavi(nomo: SintaksoArbo): Pair<String, Certeco> {
            alirilaro.setLightBulbOn(nomo.radiko, true)
            return "to des $nomo testos mave" to Certeco.Regi
        }
    }

    override suspend fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "testos" -> testos()
            else -> super.invoke(eco)
        }
    }

    override suspend fun invoke(eco: SintaksoArbo, valuo: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "gremina" -> {
                if (valuo.radiko == "buve") {
                    alirilaro.setLightBulbOn(nomo, false)
                } else {
                    alirilaro.setLightBulbBrightness(
                            nomo,
                            Nombroj.legiNombron(valuo)
                    )
                }
                "to des $nomo gremina $valuo" to Certeco.Regi
            }
            else -> super.invoke(eco, valuo)
        }
    }

    private suspend fun testos(): Pair<String, Certeco> {
        val respondo = alirilaro.getLightBulbState(nomo)
        val valuo = if (respondo.on) "mave" else "buve"
        return valuo to Certeco.Sagi
    }
}

internal object minero : Lumo {
    override val nomo = "minero"
}