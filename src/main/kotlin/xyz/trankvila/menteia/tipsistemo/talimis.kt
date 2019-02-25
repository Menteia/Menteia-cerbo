package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.vorttrakto.Legilo

class talimis(val _nomo: String): _negiTipo() {
    var enhavo = alirilaro.alportiListon(_nomo)?.map {
        Legilo.legi(it)
    }?.toMutableList()

    init {
        if (enhavo == null) {
            alirilaro.kreiListon(_nomo)
            enhavo = mutableListOf()
        }
    }

    fun diremi(): kamis {
        return lemis.ciferigi(enhavo!!.size.toBigInteger())
    }

    override suspend fun _valuigi(): List<timis>? {
        return enhavo
    }

    override suspend fun _simpligi(): timis? {
        return if (enhavo == null) {
            null
        } else {
            brotas.igiListon(enhavo!!)
        }
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

class las(val _listo: talimis, val _indekso: kamis): _negiTipo(_listo, _indekso) {
    override suspend fun _valuigi(): timis {
        return _listo.enhavo!![_indekso._valuo.numeratorAsInt]
    }

    override suspend fun _simpligi(): timis {
        return _valuigi()
    }
}

class karema(val _listo: talimis, val _aĵo: timis, val _loko: kamis): gremis(_listo::class, _listo, _aĵo, _loko) {
    override fun _ekruli(): Deferred<vanemis.tadumis<out timis>> {
        _listo.enhavo!!.add(_loko._valuo.numeratorAsInt, _aĵo)
        alirilaro.redaktiListon(_listo._nomo, _listo.enhavo!!.map {
            it.toString()
        })
        return CompletableDeferred(to(las(_listo, _loko), _aĵo))
    }
}

class kirema(val _listo: talimis, val _aĵo: timis): gremis(_listo::class) {
    override fun _ekruli(): Deferred<vanemis.tadumis<out timis>> {
        _listo.enhavo!!.add(_aĵo)
        alirilaro.redaktiListon(_listo._nomo, _listo.enhavo!!.map {
            it.toString()
        })
        return CompletableDeferred(to(las(_listo, lemis.ciferigi(_listo.enhavo!!.size.toBigInteger())), _aĵo))
    }
}

class kurinis(_listo: talimis, _aĵo: timis): vanemis.tadumis<timis>({
    _listo.enhavo!!.contains(_aĵo)
}, _listo, _listo, _aĵo)

class karisi(val _listo: talimis, val _aĵo: timis): gremis(_listo::class) {
    override fun _ekruli(): Deferred<vanemis.tadumis<out timis>> {
        val sukcesa = _listo.enhavo!!.remove(_aĵo)
        if (sukcesa) {
            return CompletableDeferred(to(des(_listo, "direma"),
                    lemis.ciferigi(_listo.enhavo!!.size.toBigInteger()))
            )
        } else {
            throw MenteiaTipEkcepcio(pegi(klos(kurinis(_listo, _aĵo))))
        }
    }
}