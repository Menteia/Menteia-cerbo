package xyz.`5atm`.menteia.cerbo.girisa

import xyz.`5atm`.menteia.cerbo.Certeco
import xyz.`5atm`.menteia.cerbo.Iloj
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import xyz.`5atm`.menteia.cerbo.NomitaAĵo
import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.MenteiaEksepcio
import xyz.`5atm`.menteia.datumo.alirilaro

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
            val kvanto = alirilaro.nombriListojn()
            return Nombroj.nombrigi(kvanto) to Certeco.Negi
        }

        fun kurimis(listoNomo: SintaksoArbo, aĵo: SintaksoArbo): Pair<String, Certeco> {
            val listo = alirilaro.alportiListon(listoNomo.radiko)
            if (listo.contains(aĵo.toString())) {
                return "paranas" to Certeco.Negi
            } else {
                return "klos paranas" to Certeco.Negi
            }
        }

        fun tremos(aĵo: SintaksoArbo): Pair<String, Certeco> {
            return when (aĵo.radiko) {
                "girisa" -> {
                    val listoj = alirilaro.ĉiujListoj()
                    Iloj.listigi(listoj.map { it.key }) to Certeco.Negi
                }
                else -> throw Exception("$aĵo ne estas aro")
            }
        }

        fun karema(listoNomo: SintaksoArbo, aĵo: SintaksoArbo, nombro: SintaksoArbo): Pair<String, Certeco> {
            val listo = alirilaro.alportiListon(listoNomo.radiko)
            val indekso = Nombroj.legiNombron(nombro)
            if (indekso !in 0..listo.size) {
                throw MenteiaEksepcio("klos pokes $nombro mora ${Nombroj.nombrigi(listo.size)}")
            }
            val novaListo = listo.toMutableList().apply {
                add(indekso, aĵo.toString())
            }
            alirilaro.redaktiListon(listoNomo.radiko, novaListo)
            return "miras des $listoNomo las $nombro $aĵo" to Certeco.Negi
        }

        fun kirema(listoNomo: SintaksoArbo, aĵo: SintaksoArbo): Pair<String, Certeco> {
            val listo = alirilaro.alportiListon(listoNomo.radiko)
            val novaListo = listo.toMutableList().apply {
                add(aĵo.toString())
            }
            alirilaro.redaktiListon(listoNomo.radiko, novaListo)
            return "miras des $listoNomo las ${Nombroj.nombrigi(novaListo.size-1)} $aĵo" to Certeco.Negi
        }

        fun karisi(listoNomo: SintaksoArbo, aĵo: SintaksoArbo): Pair<String, Certeco> {
            val nomo = listoNomo.radiko
            val listo = alirilaro.alportiListon(listoNomo.radiko)
            val novaListo = listo.toMutableList()
            if (novaListo.remove(aĵo.toString())) {
                alirilaro.redaktiListon(listoNomo.radiko, novaListo)
                return "miras des $nomo diremi ${Nombroj.nombrigi(novaListo.size)}" to Certeco.Negi
            } else {
                throw MenteiaEksepcio("klos kurimis $listoNomo $aĵo")
            }
        }

        fun marina(): Pair<String, Certeco> {
            val novaNomo = alirilaro.kreiListon()
            return "tinas $novaNomo" to Certeco.Negi
        }

        fun furika(nomo: SintaksoArbo): Pair<String, Certeco> {
            alirilaro.forigiListon(nomo.radiko)
            return "klos sindis $nomo" to Certeco.Negi
        }
    }

    override fun priskribi(): Pair<String, Certeco> {
        val listo = alirilaro.alportiListon(nomo)
        return Iloj.listigi(listo) to Certeco.Negi
    }

    override suspend fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        return when (eco.radiko) {
            "diremi" -> diremi()
            "las" -> las(eco.opcioj[0])
            else -> super.invoke(eco)
        }
    }

    private fun diremi(): Pair<String, Certeco> {
        val listo = alirilaro.alportiListon(nomo)
        return Nombroj.nombrigi(listo.size) to Certeco.Negi
    }

    private fun las(nombro: SintaksoArbo): Pair<String, Certeco> {
        val indekso = Nombroj.legiNombron(nombro)
        val listo = alirilaro.alportiListon(nomo)
        if (indekso in 0 until listo.size) {
            return listo[indekso] to Certeco.Negi
        } else {
            throw MenteiaEksepcio("klos tinas des $nomo las $nombro")
        }
    }
}