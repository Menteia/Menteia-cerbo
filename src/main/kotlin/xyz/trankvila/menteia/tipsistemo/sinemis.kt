package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.tipsistemo.interna._kreebla

class sinemis(
        val _nomo: String
): timis(), _kreebla {
    override suspend fun _valuigi(): Pair<String, String> {
        return alirilaro.alportiLokon(_nomo)
    }

    override fun toString(): String {
        return _nomo
    }

    override fun traversi(): Sequence<String> {
        return sequence {
            yield(_nomo)
        }
    }
}