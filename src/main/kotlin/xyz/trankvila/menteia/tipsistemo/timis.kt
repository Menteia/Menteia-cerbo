package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.runBlocking
import xyz.trankvila.menteia.vorttrakto.antaŭpaŭzoj
import xyz.trankvila.menteia.vorttrakto.interpaŭzoj

interface _bazaTipo {
    suspend fun _valuigi(): Any?
    suspend fun _simpligi(): timis?
}

abstract class timis(
        val morem: Any? = null,
        val ponem: Any? = null,
        val forem: Any? = null
): _bazaTipo {
    override fun equals(other: Any?): Boolean {
        return runBlocking {
            when (other) {
                is timis -> {
                    _valuigi() == other._valuigi()
                }
                else -> super.equals(other)
            }
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

class MenteiaTipEkcepcio(val _mesaĝo: timis): Exception(_mesaĝo.toString())