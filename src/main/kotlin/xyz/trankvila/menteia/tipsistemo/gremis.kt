package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.Deferred
import kotlin.reflect.KClass

abstract class gremis(
        val _frazo: KClass<out timis>,
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): timis(morem, ponem, forem) {
    override suspend fun _valuigi(): Nothing {
        throw Exception("Ago ne havas valuon")
    }

    abstract fun _ekruli(): Deferred<vanemis.tadumis<out timis>>
}