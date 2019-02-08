package xyz.trankvila.menteia.cerbo.girisa

import xyz.trankvila.menteia.cerbo.Certeco
import xyz.trankvila.menteia.cerbo.Iloj
import xyz.trankvila.menteia.cerbo.kiram.Nombroj
import xyz.trankvila.menteia.cerbo.NomitaAĵo
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.MenteiaEksepcio
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.cerbo.Cerbo

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
            val indekso = Nombroj.legiNombron(nombro).toInt()
            if (indekso !in 0..listo.size) {
                throw MenteiaEksepcio("klos pokes $nombro mora ${Nombroj.nombrigi(listo.size)}")
            }
            val novaListo = listo.toMutableList().apply {
                add(indekso, aĵo.toString())
            }
            alirilaro.redaktiListon(listoNomo.radiko, novaListo)
            return "to des $listoNomo las $nombro $aĵo" to Certeco.Megi
        }

        fun kirema(listoNomo: SintaksoArbo, aĵo: SintaksoArbo): Pair<String, Certeco> {
            val listo = alirilaro.alportiListon(listoNomo.radiko)
            val novaListo = listo.toMutableList().apply {
                add(aĵo.toString())
            }
            alirilaro.redaktiListon(listoNomo.radiko, novaListo)
            return "to des $listoNomo las ${Nombroj.nombrigi(novaListo.size-1)} $aĵo" to Certeco.Megi
        }

        fun karisi(listoNomo: SintaksoArbo, aĵo: SintaksoArbo): Pair<String, Certeco> {
            val nomo = listoNomo.radiko
            val listo = alirilaro.alportiListon(listoNomo.radiko)
            val novaListo = listo.toMutableList()
            if (novaListo.remove(aĵo.toString())) {
                alirilaro.redaktiListon(listoNomo.radiko, novaListo)
                return "klos kurimis $nomo $aĵo" to Certeco.Megi
            } else {
                throw MenteiaEksepcio("klos kurimis $listoNomo $aĵo")
            }
        }

        fun marina(): Pair<String, Certeco> {
            val novaNomo = alirilaro.kreiListon()
            return "tinas $novaNomo" to Certeco.Megi
        }

        fun furika(nomo: SintaksoArbo): Pair<String, Certeco> {
            alirilaro.forigiListon(nomo.radiko)
            return "klos sindis $nomo" to Certeco.Megi
        }

        suspend fun vidina(listoNomo: SintaksoArbo): Pair<String, Certeco> {
            val listo = alirilaro.alportiListon(listoNomo.radiko)
            val respondoj = listo.map {
                val arbo = SintaksoArbo.konstrui(it)
                when (arbo.radiko) {
                    "ko" -> Cerbo.ko(arbo.opcioj[0]).first
                    else -> throw Exception("Ne komprenis $arbo")
                }
            }
            return Iloj.listigi(respondoj) to Certeco.Pegi
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
        val indekso = Nombroj.legiNombron(nombro).toInt()
        val listo = alirilaro.alportiListon(nomo)
        if (indekso in 0 until listo.size) {
            return listo[indekso] to Certeco.Negi
        } else {
            throw MenteiaEksepcio("klos tinas des $nomo las $nombro")
        }
    }
}