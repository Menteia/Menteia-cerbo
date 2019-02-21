package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking

abstract class _negiTipo(
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): timis(morem, ponem, forem)

abstract class _sagiTipo(
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): timis(morem, ponem, forem)

abstract class vidimis(
        val _valuo: Deferred<vanemis>,
        ponem: Any? = null,
        forem: Any? = null
): timis(_valuo, ponem, forem) {
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
            is vanemis.tadumis<*> -> {
                val respondo = if (_valuo._valuo()) {
                    _valuo
                } else {
                    klos(_valuo)
                }
                return when (_valuo._frazo) {
                    is des -> {
                        when (_valuo._frazo._objekto) {
                            is _sagiTipo -> {
                                sagi(respondo)
                            }
                            is _negiTipo -> {
                                negi(respondo)
                            }
                            else -> throw Exception("Ne eblas simpligi $this")
                        }
                    }
                    is _sagiTipo -> {
                        sagi(respondo)
                    }
                    is _negiTipo -> {
                        negi(respondo)
                    }
                    else -> throw Exception("Ne eblas simpligi $this")
                }
            }
            is vanemis.fragemis<*> -> {
                val demando = _valuo._valuo
                when (demando) {
                    is des -> {
                        when (demando._objekto) {
                            is _negiTipo -> {
                                negi(to(demando, demando._valuigi() as renas))
                            }
                            is _sagiTipo -> {
                                sagi(to(demando, demando._valuigi() as renas))
                            }
                            else -> throw Exception("Ne eblas respondi al $this")
                        }
                    }
                    is _negiTipo -> {
                        val rezulto = demando._simpligi()
                        return if (rezulto == null) {
                            negi(klos(sindis(demando)))
                        } else {
                            negi(to(demando, rezulto))
                        }
                    }
                    is _sagiTipo -> {
                        val rezulto = demando._simpligi()
                        if (rezulto != null) {
                            sagi(to(demando, rezulto))
                        } else {
                            sagi(klos(sindis(demando)))
                        }
                    }
                    else -> throw Exception("Ne eblas respondi al $this")
                }
            }
        }
    }
}

class keli(val _ago: gremis): vidimis(_ago._ekruli()) {
    override suspend fun _valuigi(): vidimis {
        val rezulto = _valuo.await()
        return when (rezulto) {
            is vanemis.tadumis<*> -> {
                val objekto = _ago._frazo
                when (objekto) {
                    is _negiTipo -> {
                        megi(rezulto)
                    }
                    is _sagiTipo -> {
                        regi(rezulto)
                    }
                    else -> throw Exception("Ne eblas montri la rezulton de $_ago")
                }
            }
            else -> throw Exception("Ne eblas ekruli la agon $_ago")
        }
    }
}

class negi(morem: vanemis.tadumis<out timis>): vidimis(CompletableDeferred(morem))

class sagi(morem: vanemis.tadumis<out timis>): vidimis(CompletableDeferred(morem))

class megi(morem: vanemis.tadumis<out timis>): vidimis(CompletableDeferred(morem))

class regi(morem: vanemis.tadumis<out timis>): vidimis(CompletableDeferred(morem))

class pegi(morem: vanemis.tadumis<out timis>): vidimis(CompletableDeferred(morem))