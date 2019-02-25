package xyz.trankvila.menteia.cerbo.brodimis

import kotlinx.coroutines.Job
import xyz.trankvila.menteia.cerbo.Certeco
import xyz.trankvila.menteia.cerbo.Iloj
import xyz.trankvila.menteia.cerbo.kiramis.Nombroj
import xyz.trankvila.menteia.cerbo.NomitaAĵo
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.MenteiaEksepcio
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.cerbo.Cerbo

class Listo(override val nomo: String) : NomitaAĵo {
    companion object : NomitaAĵo {
        override val nomo: String = "brodimis"

        override suspend fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
            return when (eco.radiko) {
                "diremi" -> diremi()
                else -> super.invoke(eco)
            }
        }

        private fun diremi(): Pair<String, Certeco> {
            val kvanto = alirilaro.nombriListojn()
            return Nombroj.nombrigi(kvanto) to Certeco.Negi
        }

        fun kurimis(listoNomo: SintaksoArbo, aĵo: SintaksoArbo): Pair<String, Certeco> {
            TODO()
        }

        fun tremos(aĵo: SintaksoArbo): Pair<String, Certeco> {
            return when (aĵo.radiko) {
                "brodimis" -> {
                    val listoj = alirilaro.ĉiujListoj()
                    Iloj.listigi(listoj.map { it.key }) to Certeco.Negi
                }
                else -> throw Exception("$aĵo ne estas aro")
            }
        }

        fun karema(listoNomo: SintaksoArbo, aĵo: SintaksoArbo, nombro: SintaksoArbo): Pair<String, Certeco> {
            TODO()
        }

        fun kirema(listoNomo: SintaksoArbo, aĵo: SintaksoArbo): Pair<String, Certeco> {
            TODO()
        }

        fun karisi(listoNomo: SintaksoArbo, aĵo: SintaksoArbo): Pair<String, Certeco> {
            TODO()
        }

        fun marina(tipo: SintaksoArbo): Pair<String, Certeco> {
            val novaNomo = alirilaro.kreiListon(tipo.toString())
            return "sindis $novaNomo" to Certeco.Megi
        }

        fun furika(nomo: SintaksoArbo): Pair<String, Certeco> {
            alirilaro.forigiListon(nomo.radiko)
            return "klos sindis $nomo" to Certeco.Megi
        }

        suspend fun vidina(listoNomo: SintaksoArbo, sekvaMesaĝo: (String) -> Job): Pair<String, Certeco> {
            TODO()
        }
    }

    override fun priskribi(): Pair<String, Certeco> {
        TODO()
    }

    override suspend fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "diremi" -> diremi()
            "las" -> las(eco.opcioj[0])
            else -> super.invoke(eco)
        }
    }

    private fun diremi(): Pair<String, Certeco> {
        TODO()
    }

    private fun las(nombro: SintaksoArbo): Pair<String, Certeco> {
        TODO()
    }
}