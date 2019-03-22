package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.runBlocking
import org.apache.commons.math3.fraction.BigFraction
import xyz.trankvila.menteia.tipsistemo.interna._certeco
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

abstract class lemis(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
): timis(morem, ponem, forem) {
    override val _tipo = _certeco.negi
    protected abstract val _valuo: BigFraction

    companion object {
        private val nombroj = listOf(::mira, ::pona, ::fora, ::nona, ::tera, ::sina, ::lira, ::ŝona, ::kera, ::gina)
        private val partnombroj = listOf(::miri, ::poni, ::fori, ::noni, ::teri, ::sini, ::liri, ::ŝoni, ::keri, ::gini)
        private val prefiksoj = mapOf(
                6 to ::sariga,
                3 to ::ponega
        )

        fun nombrigi(nombro: BigDecimal, decimalciferoj: Int = 3): lemis {
            if (nombro.signum() == -1) {
                return gulos(nombrigi(-nombro, decimalciferoj))
            }
            if (nombro.signum() == 0 || nombro.scale() <= 0 || nombro.stripTrailingZeros().scale() <= 0) {
                return liris(nombrigi(nombro.toBigIntegerExact()), mira())
            } else {
                val decimalo = (nombro - nombro.setScale(0, RoundingMode.DOWN)).movePointRight(decimalciferoj).toBigInteger()
                return liris(nombrigi(nombro.toBigInteger()), ciferigi(decimalo))
            }
        }

        private fun nombrigi(nombro: BigInteger): girimis {
            if (nombro < BigInteger.TEN) {
                return nombroj[nombro.toInt()]()
            } else {
                val (prefikso, restanta) = troviPrefikson(nombro)
                return if (prefikso == null) {
                    ciferigi(restanta)
                } else {
                    prefikso(ciferigi(restanta))
                }
            }
        }

        fun ciferigi(nombro: BigInteger): kamis {
            val ciferoj = nombro.toString()
            val baza = nombroj[ciferoj.last().toString().toInt()]()
            return ciferoj.substring(0..ciferoj.length-2).foldRight(baza) { c, acc ->
                partnombroj[c.toInt() - '0'.toInt()](acc)
            }
        }

        private fun troviPrefikson(nombro: BigInteger): Pair<((kamis) -> girimis)?, BigInteger> {
            var nuloj = 0
            var restantaj = nombro

            var cifero = restantaj.remainder(BigInteger.TEN)
            while (cifero == BigInteger.ZERO) {
                nuloj += 1
                restantaj /= BigInteger.TEN
                cifero = restantaj.remainder(BigInteger.TEN)
            }

            prefiksoj.forEach {
                if (it.key in 0..nuloj) {
                    return it.value to restantaj * BigInteger.TEN.pow(nuloj - it.key)
                }
            }
            return null to nombro
        }
    }

    override suspend fun _valuigi(): BigFraction {
        return _valuo
    }

    override suspend fun _simpligi(): lemis {
        val absoluta = _valuo.abs()
        val rezulto = if (absoluta.denominator == BigInteger.ONE) {
            nombrigi(absoluta.numerator)
        } else if (absoluta.numerator == BigInteger.ONE) {
            poneras(nombrigi(absoluta.denominator))
        } else {
            generas(nombrigi(absoluta.numerator), nombrigi(absoluta.denominator))
        }
        if (_valuo.numerator.signum() == -1) {
            return gulos(rezulto)
        } else {
            return rezulto
        }
    }
}

class generas(morem: girimis, ponem: girimis): lemis(morem, ponem) {
    override val _valuo = BigFraction(
            runBlocking { morem._valuigi().numerator },
            runBlocking { ponem._valuigi().numerator }
    )
}

class poneras(morem: girimis): lemis(morem) {
    override val _valuo = BigFraction(BigInteger.ONE, runBlocking { morem._valuigi().numerator })
}

class liris(morem: girimis, ponem: kamis): lemis(morem, ponem) {
    override val _valuo = BigFraction(
            runBlocking { ponem._valuigi().numerator },
            BigInteger.TEN.pow(ponem._ciferoj.size)
    ).add(runBlocking { morem._valuigi() })
}

class gulos(morem: lemis): lemis(morem) {
    override val _valuo = runBlocking { morem._valuigi().negate() }
}

class taris(morem: lemis, ponem: lemis): lemis(morem, ponem) {
    override val _valuo = runBlocking { morem._valuigi().add(ponem._valuigi()) }
}

class kredas(morem: lemis, ponem: lemis): lemis(morem, ponem) {
    override val _valuo = runBlocking { morem._valuigi().multiply(ponem._valuigi()) }
}