package xyz.`5atm`.menteia.vorttrakto

import java.lang.Exception

data class SintaksoArbo(val radiko: String, val opcioj: List<SintaksoArbo>) {
    companion object {
        fun konstrui(
                frazo: String,
                vortoj: Iterator<String> = frazo.splitToSequence(" "
                ).iterator()): SintaksoArbo {
            if (!vortoj.hasNext()) {
                throw Exception("$frazo ne estas valida")
            }
            val sekva = vortoj.next()
            val vortaro = Vortaro.alporti()
            val vorto = vortaro[sekva] ?: throw Exception("$sekva ne estas en la vortaro")
            val valenco = vorto.valenco
            return SintaksoArbo(sekva, (0 until valenco).map {
                konstrui(frazo, vortoj)
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
                yieldAll(it.traversi())
            }
        }
    }

    override fun toString(): String {
        if (opcioj.isEmpty()) return radiko
        return "$radiko ${opcioj.map { it.toString() }.joinToString(" ")}"
    }
}