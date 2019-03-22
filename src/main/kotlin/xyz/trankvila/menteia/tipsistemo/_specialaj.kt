package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.memoro.lokajObjektoj
import xyz.trankvila.menteia.tipsistemo.interna._certeco
import xyz.trankvila.menteia.tipsistemo.interna._forigebla
import xyz.trankvila.menteia.tipsistemo.interna._nomitaAĵo
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.full.*

/**
 * Indikas econ de objekto
 * @property _objekto la objekto kiu devas enhavi la donitan econ
 * @property _eco la nomo de la eco
 */
class des(val _objekto: renas, val _eco: _nomitaAĵo): timis(
        _objekto, _eco
) {
    override val _tipo = _objekto._tipo

    override suspend fun _valuigi(): timis? {
        return _simpligi()
    }

    override suspend fun _simpligi(): timis? {
        val eco = _objekto::class.declaredMemberFunctions.find {
            it.name == _eco._nomo
        } ?: throw MenteiaTipEkcepcio(pegi(klos(sindis(this))))
        return eco.callSuspend(_objekto) as timis?
    }
}

/**
 * Agordi econ de objekto per la donita valuo
 * @property _objekto la bojekto kiu devas enhavi la donitan econ
 * @property _eco la nomo de la eco
 * @property _valuo la nova valuo por la eco
 */
class miris(val _objekto: timis, val _eco: _nomitaAĵo, val _valuo: timis): gremis(
        _objekto, _eco, _valuo
) {
    override val _tipo = _objekto._tipo

    override fun _ekruli(): Deferred<vanemis.tadumis> {
        val eco = _objekto::class.declaredMemberFunctions.find {
            it.name == _eco._nomo && it.parameters.size == 2
        } ?: throw MenteiaTipEkcepcio(pegi(klos(sindis(des(_objekto, _eco)))))
        return GlobalScope.async {
            val rezulto = eco.callSuspend(_objekto, _valuo)
            if (rezulto is vanemis.tadumis) {
                rezulto
            } else {
                throw Exception("Nekonata rezulto: $rezulto")
            }
        }
    }
}

/**
 * Krei novas objekton sen opcioj
 * @property _klaso la klaso de la objekto
 */
class marina(val _klaso: remis): gremis(_klaso) {
    override val _tipo = _certeco.negi

    override fun _ekruli(): Deferred<vanemis.tadumis> {
        return GlobalScope.async {
            val nomo = alirilaro.kreiNomon(_klaso._klaso.simpleName!!)
            val aĵo = _klaso._klaso.companionObject!!.declaredMemberFunctions.single {
                it.name == "_krei"
            }.callSuspend(_klaso._klaso.companionObjectInstance, nomo) as renas
            sindis(aĵo)
        }

    }
}

class marisa(val _klaso: remis, val _opcio: timis): gremis(_klaso, _opcio) {
    override val _tipo = _certeco.negi

    override fun _ekruli(): Deferred<vanemis.tadumis> {
        return GlobalScope.async {
            try {
                val nomo = alirilaro.kreiNomon(_klaso._klaso.simpleName!!)
                val aĵo = _klaso._klaso.companionObject!!.declaredMemberFunctions.single {
                    it.name == "_krei"
                }.callSuspend(_klaso._klaso.companionObjectInstance, nomo, _opcio) as renas
                sindis(aĵo)
            } catch (e: InvocationTargetException) {
                e.targetException.printStackTrace()
                throw e
            }
        }
    }
}

class furika(val _aĵo: _forigebla): gremis(_aĵo) {
    override val _tipo = _certeco.negi

    override fun _ekruli(): Deferred<vanemis.tadumis> {
        return GlobalScope.async {
            _aĵo._forigi()
        }
    }
}

class tremos(val _klaso: remis): timis(_klaso) {
    override val _tipo = _certeco.negi
    override suspend fun _valuigi(): Int {
        return alirilaro.nombri(this._klaso._klaso.simpleName!!)
    }

    override suspend fun _simpligi(): timis {
        return lemis.ciferigi(_valuigi().toBigInteger())
    }
}

class drumos(val _klaso: remis): timis(_klaso) {
    override val _tipo = _certeco.negi
    override suspend fun _valuigi(): List<String> {
        return alirilaro.nomi(_klaso._klaso.simpleName!!)
    }

    override suspend fun _simpligi(): timis {
        return brotas.igiListon(_valuigi().map {
            _nomitaAĵo(it)
        })
    }
}

class remis(val _klaso: KClass<out timis>): timis() {
    override suspend fun _valuigi(): KClass<out timis> {
        return _klaso
    }
}