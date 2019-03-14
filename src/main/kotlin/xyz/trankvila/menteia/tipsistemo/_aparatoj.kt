package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import xyz.trankvila.menteia.datumo.alirilaro

interface nadimis : renas

class buvi(val nadimis: nadimis): gremis(nadimis) {
    override val _tipo = _certeco.sagi

    override fun _ekruli(): Deferred<vanemis.tadumis> {
        return GlobalScope.async {
            when (nadimis) {
                is milimis -> {
                    val nomo = nadimis._nomo
                    val id = alirilaro.alportiLumon(nomo)
                    alirilaro.setLightBulbOn(id, false)
                    to(des(nadimis, _nomitaAĵo("testos")), buve())
                }
                else -> throw Exception("$nadimis ne estas elŝaltebla")
            }
        }
    }
}

class mavi(val nadimis: nadimis): gremis(nadimis) {
    override val _tipo = _certeco.sagi

    override fun _ekruli(): Deferred<vanemis.tadumis> {
        return GlobalScope.async {
            when (nadimis) {
                is milimis -> {
                    val nomo = nadimis._nomo
                    val id = alirilaro.alportiLumon(nomo)
                    alirilaro.setLightBulbOn(id, true)
                    to(des(nadimis, _nomitaAĵo("testos")), mave())
                }
                else -> throw Exception("$nadimis ne estas enŝaltebla")
            }
        }
    }
}