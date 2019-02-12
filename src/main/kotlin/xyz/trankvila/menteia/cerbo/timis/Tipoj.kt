package xyz.trankvila.menteia.cerbo.timis

import xyz.trankvila.menteia.cerbo.MenteiaEksepcio

object Tipsistemo {
    private val plejaltajTipoj = setOf(
            "timis",
            "renas",
            "morum",
            "ponum",
            "forum"
    )

    fun ĉuSupertipo(tipnomo: String, supertipnomo: String): Boolean {
        return ĉuSupertipo(legiTipon(tipnomo), legiTipon(supertipnomo))
    }

    fun ĉuSupertipo(tipo: List<String>, supertipo: List<String>): Boolean {
        if (plejaltajTipoj.contains(supertipo[0]) || tipo[0] == "vidimis") {
            return true
        }
        val vortaro = Vortaro.alporti()
        var sekva = tipo
        while (!plejaltajTipoj.contains(sekva[0])) {
            if (sekva.first() == supertipo.first()) {
                sekva.forEachIndexed { index, s ->
                    if (index > 0) {
                        if (!ĉuSupertipo(s, supertipo[index])) {
                            return false
                        }
                    }
                }
                return true
            }
            sekva = legiTipon(vortaro.getValue(sekva.first()).tipo ?:
                throw Exception("${sekva.first()} ne havas tipon"))
        }
        return false
    }

    fun legiTipon(tipo: String): List<String> {
        val vortoj = tipo.splitToSequence(" ").iterator()
        val listo = mutableListOf<String>()
        while (vortoj.hasNext()) {
            val sekva = vortoj.next()
            if (sekva == "nomis") {
                listo.add("$sekva ${vortoj.next()}")
            } else {
                listo.add(sekva)
            }
        }
        return listo
    }

    fun troviKomunanBazanTipon(tipo1: List<String>, tipo2: List<String>): List<String> {
        if (ĉuSupertipo(tipo1, tipo2)) return tipo2
        if (ĉuSupertipo(tipo2, tipo1)) return tipo1
        return listOf("timis")
    }
}

class GeneraTipilo {
    private val tipoj = mutableMapOf<String, List<String>>()
    private val fiksitaj = mutableSetOf<String>()

    companion object {
        private val nomoj = setOf("morum", "ponum", "forum")

        fun ĉuGenera(nomo: String): Boolean {
            return nomoj.contains(nomo)
        }
    }

    operator fun set(nomo: String, tipo: List<String>) {
        if (!nomoj.contains(nomo)) {
            throw Exception("$nomo ne estas valida generatipa nomo")
        }
        val ekzistantaTipo = tipoj[nomo]
        if (ekzistantaTipo != null) {
            if (fiksitaj.contains(nomo)) {
                if (!Tipsistemo.ĉuSupertipo(tipo, ekzistantaTipo)) {
                    throw MenteiaEksepcio("klos tres ${tipo.joinToString(" ")} ${ekzistantaTipo.joinToString(" ")}")
                }
            } else {
                val komunaTipo = Tipsistemo.troviKomunanBazanTipon(ekzistantaTipo, tipo)
                tipoj[nomo] = komunaTipo
            }
        } else {
            tipoj[nomo] = tipo
        }
    }

    operator fun get(nomo: String): List<String> {
        return tipoj.getValue(nomo)
    }

    fun fiksi(nomo: String, tipo: List<String>) {
        set(nomo, tipo)
        fiksitaj.add(nomo)
    }
}