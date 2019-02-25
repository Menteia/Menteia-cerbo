package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.memoro.lokajObjektoj
import kotlin.reflect.KClass
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.primaryConstructor

class _nomitaAĵo(val _nomo: String): timis(
        _nomo
) {
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

class des(val _objekto: timis, val _eco: String): timis(
        _objekto, _eco
) {
    override suspend fun _valuigi(): timis? {
        return _simpligi()
    }

    override suspend fun _simpligi(): timis? {
        val eco = _objekto::class.declaredMemberFunctions.find {
            it.name == _eco
        } ?: throw MenteiaTipEkcepcio(pegi(klos(sindis(this))))
        return eco.callSuspend(_objekto) as timis?
    }
}

class miris(val _objekto: timis, val _eco: String, val _valuo: timis): gremis(
        _objekto::class, _objekto, _eco, _valuo
) {
    override fun _ekruli(): Deferred<vanemis.tadumis<out timis>> {
        val eco = _objekto::class.declaredMemberFunctions.find {
            it.name == _eco && it.parameters.size == 2
        } ?: throw MenteiaTipEkcepcio(pegi(klos(sindis(des(_objekto, _eco)))))
        return GlobalScope.async {
            val rezulto = eco.callSuspend(_objekto, _valuo)
            if (rezulto is vanemis.tadumis<*>) {
                rezulto
            } else {
                throw Exception("Nekonata rezulto: $rezulto")
            }
        }
    }
}

class marina(val _tipo: KClass<out timis>): gremis(
        _tipo, _tipo.simpleName
) {
    override fun _ekruli(): Deferred<vanemis.tadumis<out timis>> {
        val nomo = alirilaro.kreiNomon(_tipo.simpleName!!)
        val aĵo = _tipo.primaryConstructor!!.call(nomo)
        return CompletableDeferred(sindis(aĵo))
    }
}

class marisa(val _tipo: KClass<out timis>, val _opcio: timis): gremis(
        _tipo, _tipo.simpleName, _opcio
) {
    override fun _ekruli(): Deferred<vanemis.tadumis<out timis>> {
        val nomo = alirilaro.kreiNomon(_tipo.simpleName!!)
        val aĵo = _tipo.primaryConstructor!!.call(nomo, _opcio)
        return CompletableDeferred(sindis(aĵo))
    }
}

class furika(val _aĵo: _forigibla): gremis(_aĵo::class, _aĵo) {
    override fun _ekruli(): Deferred<vanemis.tadumis<out timis>> {
        return when (_aĵo) {
            is sanimis -> {
                _aĵo._forigiSciigojn()
                alirilaro.forigiTempoŝaltilon(_aĵo._nomo)
                lokajObjektoj.remove(_aĵo._nomo)
                CompletableDeferred(klos(sindis(_aĵo)))
            }
            else -> throw Exception("$_aĵo ne estas forigebla")
        }
    }
}

class tremos(val _tipo: KClass<out timis>): _negiTipo(_tipo) {
    override suspend fun _valuigi(): Int {
        return alirilaro.nombri(_tipo.simpleName!!)
    }

    override suspend fun _simpligi(): timis? {
        return lemis.ciferigi(_valuigi().toBigInteger())
    }
}