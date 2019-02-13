package xyz.trankvila.menteia.tipsistemo

abstract class _negiTipo(
        morem: timis? = null,
        ponem: timis? = null,
        forem: timis? = null
): timis(morem, ponem, forem) {
    open fun _simpligi(): _negiTipo {
        return this
    }
}

abstract class _sagiTipo(
        morem: timis? = null,
        ponem: timis? = null,
        forem: timis? = null
): timis(morem, ponem, forem) {
    open fun _simpligi(): _sagiTipo {
        return this
    }
}

abstract class vidimis(
        val _valuo: vanemis,
        ponem: timis? = null,
        forem: timis? = null
): timis(_valuo, ponem, forem) {
    override fun _valuigi(): Any {
        return _valuo._valuigi()
    }
}

class doni(morem: vanemis): vidimis(morem) {
    override fun _valuigi(): vidimis {
        return when (_valuo) {
            is vanemis.tadumis<*> -> {
                val respondo = if (_valuo._valuo) {
                    _valuo
                } else {
                    klos(_valuo)
                }
                return when (_valuo._frazo) {
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
                    is _negiTipo -> {
                        negi(to(demando, demando._simpligi()))
                    }
                    is _sagiTipo -> {
                        sagi(to(demando, demando._simpligi()))
                    }
                    else -> throw Exception("Ne eblas simpligi $this")
                }
            }
        }
    }
}

class negi(morem: vanemis.tadumis<out timis>): vidimis(morem)

class sagi(morem: vanemis.tadumis<out timis>): vidimis(morem)