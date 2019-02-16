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
            throw Exception("Ne plu uzita")
        }
    }

    fun traversi(kunPaŭzoj: Boolean = false): Sequence<String> {
        return sequence {
            val vorto = Vortaro.alporti()[radiko]
            if (vorto == null) {
                throw Exception("$radiko ne estas en la vortaro")
            }
            yield(radiko)
            opcioj.forEach {
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