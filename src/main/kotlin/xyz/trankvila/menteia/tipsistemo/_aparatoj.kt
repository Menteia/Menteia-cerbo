package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import xyz.trankvila.menteia.datumo.alirilaro

interface nadimis : renas

class buvi(val nadimis: nadimis): gremis(nadimis::class) {
    override fun _ekruli(): Deferred<vanemis.tadumis<out timis>> {
        return GlobalScope.async {
            when (nadimis) {
                is milimis -> {
                    val nomo = nadimis._nomo
                    val id = alirilaro.alportiLumon(nomo)
                    alirilaro.setLightBulbOn(id, false)
                    to(des(nadimis, "testos"), buve())
                }
                else -> throw Exception("$nadimis ne estas elŝaltebla")
            }
        }
    }
}

class mavi(val nadimis: nadimis): gremis(nadimis::class) {
    override fun _ekruli(): Deferred<vanemis.tadumis<out timis>> {
        return GlobalScope.async {
            when (nadimis) {
                is milimis -> {
                    val nomo = nadimis._nomo
                    val id = alirilaro.alportiLumon(nomo)
                    alirilaro.setLightBulbOn(id, true)
                    to(des(nadimis, "testos"), mave())
                }
                else -> throw Exception("$nadimis ne estas enŝaltebla")
            }
        }
    }
}