package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.datumo.alirilaro

interface merimis : renas

interface melemis : renas

abstract class _modo(
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
) : timis(morem, ponem, forem) {
    override suspend fun _valuigi(): _modo {
        return this
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is _modo -> {
                this::class == other::class
            }
            else -> super.equals(other)
        }
    }
}

class milimis(val _nomo: String): _sagiTipo(), nadimis {
    override suspend fun _valuigi(): milimis {
        return this
    }

    suspend fun testos(): merimis {
        val id = alirilaro.alportiLumon(_nomo)
        val rezulto = alirilaro.getLightBulbState(id)
        return if (rezulto.on) {
            mave()
        } else {
            buve()
        }
    }

    fun gremina(): melemis {
        return buve()
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

class buve : _modo(), merimis, melemis, kredrimis
class mave : _modo(), merimis, melemis
class marisas(val _valuo: lemis): _modo(_valuo), melemis {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is marisas -> {
                _valuo == other._valuo
            }
            else -> super.equals(other)
        }
    }
}