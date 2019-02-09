package xyz.trankvila.menteia.vorttrakto

import java.lang.Exception

data class SintaksoArbo(val radiko: String, val opcioj: List<SintaksoArbo>, val tipo: String? = null) {
    companion object {
        fun konstrui(
                frazo: String,
                vortoj: Iterator<String> = frazo.splitToSequence(" "
                ).iterator(),
                bezonataTipo: String? = null): SintaksoArbo {
            if (!vortoj.hasNext()) {
                throw Exception("$frazo ne estas valida")
            }
            val sekva = vortoj.next()
            val vortaro = Vortaro.alporti()
            val vorto = vortaro[sekva] ?: throw Exception("$sekva ne estas en la vortaro")
            val valenco = vorto.valenco
            return SintaksoArbo(sekva, (0 until valenco).map {
                try {
                    konstrui(frazo, vortoj)
                } catch (e: Exception) {
                    println("Eraro okazis post $sekva")
                    throw e
                }
            })
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
        if (opcioj.size == 0) return 1
        return opcioj.fold(1) { acc, arbo ->
            acc + arbo.longeco()
        }
    }

    override fun toString(): String {
        if (opcioj.isEmpty()) return radiko
        return "$radiko ${opcioj.map { it.toString() }.joinToString(" ")}"
    }
}