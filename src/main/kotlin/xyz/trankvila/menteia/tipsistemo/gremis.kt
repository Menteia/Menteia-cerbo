package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.Deferred
import kotlin.reflect.KClass

abstract class gremis(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
): timis(morem, ponem, forem) {
    abstract override val _tipo: _certeco

    override suspend fun _valuigi(): Nothing {
        throw Exception("Ago ne havas valuon")
    }

    abstract fun _ekruli(): Deferred<vanemis.tadumis>
}