package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.tipsistemo.interna._certeco
import xyz.trankvila.menteia.tipsistemo.interna._modo

interface merimis : renas

interface melemis : renas

class milimis(val _nomo: String): timis(), nadimis {
    override val _tipo = _certeco.sagi

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
class maridas(val _valuo: lemis): _modo(_valuo), melemis {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is maridas -> {
                _valuo == other._valuo
            }
            else -> super.equals(other)
        }
    }
}