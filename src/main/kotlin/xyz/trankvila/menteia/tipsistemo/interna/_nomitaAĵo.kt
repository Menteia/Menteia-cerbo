package xyz.trankvila.menteia.tipsistemo.interna

import xyz.trankvila.menteia.tipsistemo.timis

/**
 * Reprezentas vorton, kiu ne ekzistas sendependece
 * @property _nomo la vorto
 */
class _nomitaAĵo(val _nomo: String): timis() {
    override suspend fun _valuigi(): String {
        return _nomo
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