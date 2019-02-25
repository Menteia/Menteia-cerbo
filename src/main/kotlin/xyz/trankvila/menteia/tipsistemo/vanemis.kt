package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.vorttrakto.Legilo
import kotlin.reflect.KClass

sealed class vanemis(
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): timis(morem, ponem, forem) {
    abstract class tadumis<morum: timis>(
            val _valuo: suspend () -> Boolean,
            val _frazo: morum?,
            ponem: Any? = null,
            forem: Any? = null
    ): vanemis(_frazo, ponem, forem) {
        override suspend fun _valuigi(): Boolean {
            return _valuo()
        }
    }

    abstract class fragemis<morum: renas>(
            val _valuo: morum,
            morem: Any? = null,
            ponem: Any? = null,
            forem: Any? = null
    ): vanemis(morem, ponem, forem)
}

class to(morem: timis, ponem: renas): vanemis.tadumis<timis>(
        { morem._egala(ponem) },
        morem, ponem
)

class ko<morum: renas>(morem: morum): vanemis.fragemis<morum>(
        morem,
        morem
) {
    override suspend fun _valuigi(): Any? {
        return _valuo._valuigi()
    }
}

class klos(morem: vanemis.tadumis<*>): vanemis.tadumis<timis>(
        { !morem._valuo() },
        morem
)

class tres(morem: timis, ponem: KClass<*>): vanemis.tadumis<timis>(
        { ponem.isInstance(morem) },
        morem, Legilo._nomoKunTipaktantoj(ponem)
)

class sindis(_aĵo: timis?): vanemis.tadumis<timis>({ _aĵo != null && _aĵo !is _nomitaAĵo }, _aĵo)