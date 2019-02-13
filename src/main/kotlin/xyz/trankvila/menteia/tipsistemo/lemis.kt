package xyz.trankvila.menteia.tipsistemo

import org.apache.commons.math3.fraction.BigFraction
import java.lang.Exception
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KFunction

abstract class lemis(
        open val _valuo: BigFraction,
        morem: timis? = null,
        ponem: timis? = null,
        forem: timis? = null
): _negiTipo(morem, ponem, forem) {
    companion object {
        private val nombroj = listOf(::mira, ::pona, ::fora, ::nona, ::tera, ::sina, ::lira, ::ŝona, ::kera, ::gina)
        private val partnombroj = listOf(::miri, ::poni, ::fori, ::noni, ::teri, ::sini, ::liri, ::ŝoni, ::keri, ::gini)
        private val prefiksoj = mapOf(
                6 to ::sariga,
                3 to ::ponega
        )

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

        private fun ciferigi(nombro: BigInteger): kamis {
            val ciferoj = nombro.toString()
            val baza = nombroj[ciferoj.last().toInt()]()
            return ciferoj.substring(0..ciferoj.length-2).fold(baza) { acc, c ->
                partnombroj[c.toInt()](acc)
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
                    return it.value to restantaj
                }
            }
            return null to restantaj
        }
    }

    override fun _valuigi(): BigFraction {
        return _valuo
    }

    override fun _simpligi(): lemis {
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

class generas(morem: girimis, ponem: girimis): lemis(
        BigFraction(
                morem._valuigi().numerator,
                ponem._valuigi().numerator
        ),
        morem,
        ponem
)

class poneras(morem: girimis): lemis(
        BigFraction(BigInteger.ONE, morem._valuigi().numerator),
        morem
)

class liris(morem: girimis, ponem: kamis): lemis(
        BigFraction(
                ponem._valuigi().numerator,
                BigInteger.TEN.pow(ponem._ciferoj.size)
        ).add(morem._valuigi()),
        morem,
        ponem
)

class gulos(morem: lemis): lemis(
        morem._valuigi().negate(),
        morem
)

class taris(morem: lemis, ponem: lemis): lemis(
        morem._valuigi().add(ponem._valuo),
        morem,
        ponem
)

class kredas(morem: lemis, ponem: lemis): lemis(
        morem._valuigi().multiply(ponem._valuo),
        morem,
        ponem
)