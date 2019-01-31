package xyz.`5atm`.menteia.cerbo.girisa

import xyz.`5atm`.menteia.cerbo.Certeco
import xyz.`5atm`.menteia.cerbo.Iloj
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import xyz.`5atm`.menteia.cerbo.NomitaAĵo
import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import alportiListon
import nombriListojn

class Listo(override val nomo: String) : NomitaAĵo {
    companion object : NomitaAĵo {
        override val nomo: String = "girisa"

        override suspend fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
            return when (eco.radiko) {
                "diremi" -> diremi()
                else -> super.invoke(eco)
            }
        }

        private fun diremi(): Pair<String, Certeco> {
            val kvanto = nombriListojn()
            return Nombroj.nombrigi(kvanto) to Certeco.Negi
        }

        fun kurimis(listoNomo: SintaksoArbo, aĵo: SintaksoArbo): Pair<String, Certeco> {
            val listo = alportiListon(listoNomo.radiko)
            if (listo.contains(aĵo.toString())) {
                return "paranas" to Certeco.Negi
            } else {
                return "klos paranas" to Certeco.Negi
            }
        }
    }

    override fun priskribi(): Pair<String, Certeco> {
        val listo = alportiListon(nomo)
        return Iloj.listigi(listo) to Certeco.Negi
    }

    override suspend fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "diremi" -> diremi()
            else -> super.invoke(eco)
        }
    }

    private fun diremi(): Pair<String, Certeco> {
        val listo = alportiListon(nomo)
        return Nombroj.nombrigi(listo.size) to Certeco.Negi
    }
}