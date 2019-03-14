package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import xyz.trankvila.menteia.memoro.Memoro
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.isSubclassOf

abstract class vidimis(
        val _valuo: Deferred<vanemis>
): timis() {
    override suspend fun _valuigi(): Any? {
        return _valuo.await()._valuigi()
    }

    override fun toString(): String {
        val rezulto = runBlocking {
            _valuo.await()
        }
        return listOf(
                this::class.simpleName!!,
                rezulto.toString()
        ).joinToString(" ")
    }

    override fun traversi(): Sequence<String> {
        val rezulto = runBlocking {
            _valuo.await()
        }
        return sequence {
            yield(this@vidimis::class.simpleName!!)
            yieldAll(rezulto.traversi())
        }
    }
}

class doni(morem: vanemis): vidimis(CompletableDeferred(morem)) {
    override suspend fun _valuigi(): vidimis {
        val _valuo = _valuo.await()
        return when (_valuo) {
            is vanemis.tadumis -> {
                val respondo = if (_valuo._valuigi()) {
                    _valuo
                } else {
                    klos(_valuo)
                }
                val frazo = _valuo._frazo
                return when (frazo._tipo) {
                    _certeco.sagi -> {
                        sagi(respondo)
                    }
                    _certeco.negi -> {
                        negi(respondo)
                    }
                    else -> throw Exception("pegi ne estas ebla: $respondo")
                }
            }
            is vanemis.fragemis -> {
                val demando = _valuo._valuo
                when (demando._tipo) {
                    _certeco.negi -> {
                        val rezulto = demando._simpligi()
                        if (demando is timis) {
                            Memoro.lastaValuo = demando
                        }
                        if (rezulto == null) {
                            negi(klos(sindis(paranas())))
                        } else {
                            negi(to(paranas(), rezulto))
                        }
                    }
                    _certeco.sagi -> {
                        val rezulto = demando._simpligi()
                        if (demando is timis) {
                            Memoro.lastaValuo = demando
                        }
                        if (rezulto != null) {
                            sagi(to(paranas(), rezulto))
                        } else {
                            sagi(klos(sindis(demando)))
                        }
                    }
                    else -> throw Exception("pegi ne estas ebla: $demando")
                }
            }
        }
    }
}

class keli(val _ago: gremis): vidimis(_ago._ekruli()) {
    override suspend fun _valuigi(): vidimis {
        val rezulto = _valuo.await()
        return when (rezulto) {
            is vanemis.tadumis -> {
                when (rezulto._frazo._tipo) {
                    _certeco.negi -> {
                        megi(rezulto)
                    }
                    _certeco.sagi -> {
                        regi(rezulto)
                    }
                    _certeco.pegi -> {
                        pegi(rezulto)
                    }
                }
            }
            else -> throw Exception("Ne eblas ekruli la agon $_ago")
        }
    }
}

class negi(morem: vanemis.tadumis): vidimis(CompletableDeferred(morem))

class sagi(morem: vanemis.tadumis): vidimis(CompletableDeferred(morem))

class megi(morem: vanemis.tadumis): vidimis(CompletableDeferred(morem))

class regi(morem: vanemis.tadumis): vidimis(CompletableDeferred(morem))

class pegi(morem: vanemis.tadumis): vidimis(CompletableDeferred(morem))