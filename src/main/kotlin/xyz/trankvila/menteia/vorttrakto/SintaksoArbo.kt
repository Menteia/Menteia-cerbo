package xyz.trankvila.menteia.vorttrakto

import xyz.trankvila.menteia.cerbo.MenteiaEksepcio
import xyz.trankvila.menteia.cerbo.timis.GeneraTipilo
import xyz.trankvila.menteia.cerbo.timis.Tipsistemo
import java.lang.Exception

data class SintaksoArbo(val radiko: String, val opcioj: List<SintaksoArbo>, val tipo: List<String> = listOf()) {
    companion object {
        fun konstrui(
                frazo: String,
                vortoj: Iterator<String> = frazo.splitToSequence(" "
                ).iterator(),
                bezonataTipo: String? = null): SintaksoArbo {
            if (!vortoj.hasNext()) {
                throw Exception("atendas pli da vortoj post $frazo")
            }
            val sekva = vortoj.next()
            val vortaro = Vortaro.alporti()
            val vorto = vortaro[sekva] ?: throw Exception("$sekva ne estas en la vortaro")
            val valenco = vorto.valenco
            val tipnomo = vorto.tipo ?: throw Exception("${vorto.vorto} ne havas tipon")
            val aktantoj = vorto.aktantoj ?: throw Exception("${vorto.vorto} ne definas siajn aktantojn")
            val generaTipilo = GeneraTipilo()
            val subarboj = (0 until valenco).mapIndexed { indekso, _ ->
                val subarbo = konstrui(frazo, vortoj, aktantoj[indekso])
                Tipsistemo.legiTipon(aktantoj[indekso]).forEachIndexed { index, s ->
                    if (GeneraTipilo.ĉuGenera(s)) {
                        if (index == 0) {
                            generaTipilo[s] = Tipsistemo.legiTipon(subarbo.tipo[index])
                        } else {
                            generaTipilo.fiksi(s, Tipsistemo.legiTipon(subarbo.tipo[index]))
                        }
                    }
                }
                subarbo
            }
            val tipo = Tipsistemo.legiTipon(tipnomo).map {
                if (GeneraTipilo.ĉuGenera(it)) {
                    generaTipilo[it].joinToString(" ")
                } else {
                    it
                }
            }
            val arbo = SintaksoArbo(sekva, subarboj, tipo)
            if (bezonataTipo != null) {
                if (!Tipsistemo.ĉuSupertipo(tipo.joinToString(" "), bezonataTipo)) {
                    throw MenteiaEksepcio("klos tres ${arbo} $bezonataTipo")
                }
            }
            return arbo
        }
    }

    fun traversi(kunPaŭzoj: Boolean = false): Sequence<String> {
        return sequence {
            val vorto = Vortaro.alporti()[radiko]
            if (vorto == null) {
                throw Exception("$radiko ne estas en la vortaro")
            }
            if (kunPaŭzoj and vorto.antaŭpaŭzo) {
                yield("!longapaŭzo")
            }
            yield(radiko)
            opcioj.forEach {
                if (kunPaŭzoj and vorto.interpaŭzo) {
                    yield("!paŭzo")
                }
                yieldAll(it.traversi(kunPaŭzoj))
            }
        }
    }

    fun longeco(): Int {
        if (opcioj.isEmpty()) return 1
        return opcioj.fold(1) { acc, arbo ->
            acc + arbo.longeco()
        }
    }

    override fun toString(): String {
        if (opcioj.isEmpty()) return radiko
        return "$radiko ${opcioj.map { it.toString() }.joinToString(" ")}"
    }
}