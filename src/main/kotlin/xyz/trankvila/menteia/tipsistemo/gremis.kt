package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.Deferred

abstract class gremis(
        val _frazo: renas,
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): timis(morem, ponem, forem) {
    override suspend fun _valuigi(): Nothing {
        throw Exception("Ago ne havas valuon")
    }

    abstract fun _ekruli(): Deferred<vanemis.tadumis<out timis>>
}