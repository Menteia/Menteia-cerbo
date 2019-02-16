package xyz.trankvila.menteia.tipsistemo

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
        val _valuo: vanemis,
        ponem: Any? = null,
        forem: Any? = null
): timis(_valuo, ponem, forem) {
    override suspend fun _valuigi(): Any? {
        return _valuo._valuigi()
    }
}

class doni(morem: vanemis): vidimis(morem) {
    override suspend fun _valuigi(): vidimis {
        return when (_valuo) {
            is vanemis.tadumis<*> -> {
                val respondo = if (_valuo._valuo) {
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
                                negi(to(demando, demando._valuigi() as _bazaTipo))
                            }
                            is _sagiTipo -> {
                                sagi(to(demando, demando._valuigi() as _bazaTipo))
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

class negi(morem: vanemis.tadumis<out timis>): vidimis(morem)

class sagi(morem: vanemis.tadumis<out timis>): vidimis(morem)

class pegi(morem: vanemis.tadumis<out timis>): vidimis(morem)