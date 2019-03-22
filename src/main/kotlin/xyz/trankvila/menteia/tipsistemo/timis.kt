package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.tipsistemo.interna._certeco
import xyz.trankvila.menteia.vorttrakto.antaŭpaŭzoj
import xyz.trankvila.menteia.vorttrakto.interpaŭzoj

interface renas {
    suspend fun _valuigi(): Any?
    suspend fun _simpligi(): timis?
    val _tipo: _certeco
}

abstract class timis(
        val morem: renas? = null,
        val ponem: renas? = null,
        val forem: renas? = null
): renas {
    override val _tipo: _certeco get() = throw Exception("${this::class.simpleName} ne havas certeco")

    suspend fun _egala(other: Any?): Boolean {
        return when (other) {
            is timis -> {
                _valuigi() == other._valuigi()
            }
            else -> super.equals(other)
        }
    }

    override suspend fun _simpligi(): timis? {
        return this
    }

    override fun toString(): String {
        val nomo = mutableListOf(this::class.simpleName!!)
        if (morem != null) {
            nomo.add(morem.toString())
        }
        if (ponem != null) {
            nomo.add(ponem.toString())
        }
        if (forem != null) {
            nomo.add(forem.toString())
        }
        return nomo.joinToString(" ")
    }

    open fun traversi(): Sequence<String> {
        return sequence {
            if (antaŭpaŭzoj.contains(this@timis::class.simpleName!!)) {
                yield("!antaŭpaŭzo")
            }
            yield(this@timis::class.simpleName!!)
            listOf(morem, ponem, forem).forEach {
                if (it != null) {
                    if (interpaŭzoj.contains(this@timis::class.simpleName!!)) {
                        yield("!interpaŭzo")
                    }
                    when (it) {
                        is timis -> { yieldAll(it.traversi()) }
                        else -> { yield(it.toString()) }
                    }
                }
            }
        }
    }
}

internal class MenteiaTipEkcepcio(val _mesaĝo: timis): Exception(_mesaĝo.toString())