package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.tipsistemo.interna._certeco
import xyz.trankvila.menteia.tipsistemo.interna._forigebla
import xyz.trankvila.menteia.tipsistemo.interna._kreebla
import xyz.trankvila.menteia.tipsistemo.interna._nomitaAĵo
import xyz.trankvila.menteia.vorttrakto.Legilo
import java.lang.Exception

class talimis(val _nomo: String): timis(), _forigebla, _kreebla {
    companion object {
        suspend fun _krei(nomo: String): talimis {
            alirilaro.kreiListon(nomo)
            return talimis(nomo)
        }
    }

    override val _tipo = _certeco.negi
    var enhavo = GlobalScope.async {
        alirilaro.alportiListon(_nomo).map {
            Legilo.legi(it)
        }.toMutableList()
    }

    suspend fun diremi(): kamis {
        return lemis.ciferigi(enhavo.await().size.toBigInteger())
    }

    override suspend fun _valuigi(): List<timis> {
        return enhavo.await()
    }

    override suspend fun _simpligi(): timis? {
        return brotas.igiListon(enhavo.await())
    }

    override suspend fun _forigi(): vanemis.tadumis {
        alirilaro.forigiListon(_nomo)
        return klos(sindis(this))
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

class las(val _listo: talimis, val _indekso: kamis): timis(_listo, _indekso) {
    override val _tipo = _certeco.negi
    override suspend fun _valuigi(): timis {
        return _listo.enhavo.await()[_indekso._valuigi().numeratorAsInt]
    }

    override suspend fun _simpligi(): timis {
        return _valuigi()
    }
}

class karema(val _listo: talimis, val _aĵo: timis, val _loko: kamis): gremis(_listo, _aĵo, _loko) {
    override val _tipo = _certeco.negi
    override fun _ekruli(): Deferred<vanemis.tadumis> {
        return GlobalScope.async {
            val listo = _listo.enhavo.await()
            listo.add(_loko._valuigi().numeratorAsInt, _aĵo)
            alirilaro.redaktiListon(_listo._nomo, listo.map {
                it.toString()
            })
            to(las(_listo, _loko), _aĵo)
        }
    }
}

class kirema(val _listo: talimis, val _aĵo: timis): gremis(_listo) {
    override val _tipo = _certeco.negi
    override fun _ekruli(): Deferred<vanemis.tadumis> {
        return GlobalScope.async {
            val listo = _listo.enhavo.await()
            listo.add(_aĵo)
            alirilaro.redaktiListon(_listo._nomo, listo.map {
                it.toString()
            })
            to(las(_listo, lemis.ciferigi(listo.size.toBigInteger())), _aĵo)
        }
    }
}

class kurinis(val _listo: talimis, val _aĵo: timis): vanemis.tadumis(_listo, _aĵo) {
    override val _valuo: Deferred<Boolean>
        get() = GlobalScope.async {
            _listo.enhavo.await().contains(_aĵo)
        }
}

class karisi(val _listo: talimis, val _aĵo: timis): gremis(_listo) {
    override val _tipo = _certeco.negi

    override fun _ekruli(): Deferred<vanemis.tadumis> {
        return GlobalScope.async {
            val listo = _listo.enhavo.await()
            val sukcesa = listo.remove(_aĵo)
            if (sukcesa) {
                alirilaro.redaktiListon(_listo._nomo, listo.map {
                    it.toString()
                })
                to(des(_listo, _nomitaAĵo("direma")),
                        lemis.ciferigi(listo.size.toBigInteger())
                )
            } else {
                throw MenteiaTipEkcepcio(pegi(klos(kurinis(_listo, _aĵo))))
            }
        }
    }
}

class vidina(val _listo: talimis): gremis(_listo) {
    override val _tipo = _certeco.pegi

    override fun _ekruli(): Deferred<vanemis.tadumis> {
        return GlobalScope.async {
            val rezulto = _listo.enhavo.await().map {
                val r = it._simpligi()!!
                if (r is vanemis.tadumis) {
                    r
                } else {
                    throw Exception("Kalkulis: $it")
                }
            }
            tinas(brotas.igiListon(rezulto))
        }
    }
}