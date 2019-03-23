package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import xyz.trankvila.menteia.memoro.Memoro
import xyz.trankvila.menteia.tipsistemo.interna._nomitaAĵo

sealed class vanemis(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
): timis(morem, ponem, forem) {
    abstract class tadumis(
            val _frazo: renas,
            ponem: renas? = null,
            forem: renas? = null
    ): vanemis(_frazo, ponem, forem) {
        protected abstract val _valuo: Deferred<Boolean>

        override val _tipo get() = _frazo._tipo

        override suspend fun _valuigi(): Boolean {
            Memoro.lastaFrazo = this
            return _valuo.await()
        }
    }

    abstract class fragemis(
            val _valuo: renas,
            ponem: renas? = null,
            forem: renas? = null
    ): vanemis(_valuo, ponem, forem)
}

class to(val _valuo1: timis, val _valuo2: renas): vanemis.tadumis(_valuo1, _valuo2) {
    override val _valuo: Deferred<Boolean>
        get() = GlobalScope.async {
            _valuo1._egala(_valuo2)
        }
}

class ko<morum: renas>(morem: morum): vanemis.fragemis(morem) {
    override suspend fun _valuigi(): Any? {
        return _valuo._valuigi()
    }
}

class klos(_frazo: vanemis.tadumis): vanemis.tadumis(_frazo) {
    override val _valuo: Deferred<Boolean> = GlobalScope.async {
        !_frazo._valuigi()
    }
}

class tres(morem: timis, val _klaso: remis): vanemis.tadumis(morem, _klaso) {
    override val _valuo: Deferred<Boolean>
        get() = GlobalScope.async {
            _klaso._klaso.isInstance(_klaso)
        }
}

class sindis(val _aĵo: renas): vanemis.tadumis(_aĵo) {
    override val _valuo: Deferred<Boolean>
        get() = GlobalScope.async {
            _aĵo !is _nomitaAĵo
        }
}

class tinas(morem: brodimis<vanemis.tadumis>): vanemis.tadumis(morem) {
    override val _valuo = GlobalScope.async {
        morem._listo.all {
            it._valuigi()
        }
    }
}