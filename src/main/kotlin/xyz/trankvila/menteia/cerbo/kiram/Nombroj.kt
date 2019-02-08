package xyz.trankvila.menteia.cerbo.kiram

import org.apache.commons.math3.fraction.BigFraction
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.util.*

object Nombroj {
    private val nombroj = listOf("mora", "pona", "fora", "nora", "tega", "sira", "lina", "ʃona", "kera", "gina")
    private val partnombroj = listOf("mori", "poni", "fori", "nori", "tegi", "siri", "lini", "ʃoni", "keri", "gini")
    private val prefiksoj = linkedMapOf(
            6 to "sariga",
            3 to "ponega",
            -1 to "prena",
            -2 to "dreta",
            -3 to "glima"
    )
    private val maligitajPrefiksoj = prefiksoj.map {
        it.value to it.key
    }.toMap()

    fun nombrigi(nombro: Int): String {
        return nombrigi(nombro.toBigInteger())
    }

    fun nombrigi(nombro: Float, decimalciferoj: Int = 1): String {
        return nombrigi(nombro.toBigDecimal(), decimalciferoj)
    }

    fun legiNombron(arbo: SintaksoArbo): BigFraction {
        if (arbo.radiko == "gulos") {
            return legiNombron(arbo.opcioj[0]).negate()
        } else if (arbo.radiko == "poneras") {
            return BigFraction(BigInteger.ONE, legiNombron(arbo.opcioj[0]).numerator)
        } else if (arbo.radiko == "generas") {
            return BigFraction(
                    legiNombron(arbo.opcioj[0]).numerator,
                    legiNombron(arbo.opcioj[1]).numerator
            )
        }
        val prefikso = maligitajPrefiksoj[arbo.radiko]
        return if (prefikso != null) {
            BigFraction(legiCiferon(arbo.opcioj[0]) * Math.pow(10.0, prefikso.toDouble()))
        } else {
            BigFraction(legiCiferon(arbo))
        }
    }

    private fun legiCiferon(arbo: SintaksoArbo): Int {
        var nombro = 0
        arbo.traversi().forEach {
            val cifero = partnombroj.indexOf(it)
            if (cifero == -1) {
                val cifero2 = nombroj.indexOf(it)
                if (cifero2 == -1) {
                    throw Exception("Ne povas kompreni $it en $arbo")
                } else {
                    return nombro * 10 + cifero2
                }
            } else {
                nombro = nombro * 10 + cifero
            }
        }
        throw Exception("Ne povas kompreni ${arbo}")
    }

    fun nombrigi(nombro: BigInteger): String {
        if (nombro < BigInteger.ZERO) {
            return "gulos ${nombrigi(-nombro)}"
        } else if (nombro < BigInteger.TEN) {
            return nombroj[nombro.toInt()]
        } else {
            val ciferoj = Stack<String>()
            var (restantaj, prefikso) = troviPrefikson(nombro)
            while (restantaj > BigInteger.ZERO) {
                val cifero = restantaj.remainder(BigInteger.TEN)
                if (ciferoj.isEmpty()) {
                    ciferoj.push(nombroj[cifero.toInt()])
                } else {
                    ciferoj.push(partnombroj[cifero.toInt()])
                }
                restantaj = restantaj.divide(BigInteger.TEN)
            }
            val sb = StringBuilder()
            while (ciferoj.isNotEmpty()) {
                sb.append(ciferoj.pop())
                if (ciferoj.isNotEmpty()) {
                    sb.append(' ')
                }
            }
            return if (prefikso == null) sb.toString() else "$prefikso $sb"
        }
    }

    fun nombrigi(nombro: BigFraction): String {
        if (nombro.denominator == BigInteger.ONE) {
            return nombrigi(nombro.numerator)
        } else if (nombro.numerator == BigInteger.ONE) {
            return "poneras ${nombrigi(nombro.denominator)}"
        } else {
            return "generas ${nombrigi(nombro.numerator)} ${nombrigi(nombro.denominator)}"
        }
    }

    fun nombrigi(nombro: BigDecimal, decimalciferoj: Int = 3): String {
        if (nombro < BigDecimal.ZERO) {
            return "gulos ${nombrigi(-nombro, decimalciferoj)}"
        } else if (nombro.signum() == 0 || nombro.scale() <= 0 || nombro.stripTrailingZeros().scale() <= 0) {
            return nombrigi(nombro.toBigIntegerExact())
        } else {
            if (nombro < BigDecimal.ONE) {
                val prefikso = prefiksoj.getValue(-decimalciferoj)
                val decimalo = nombro.movePointRight(decimalciferoj).toBigInteger()
                return "$prefikso ${nombrigi(decimalo)}"
            } else {
                val decimalo = (nombro - nombro.setScale(0, RoundingMode.DOWN)).movePointRight(decimalciferoj).toBigInteger()
                return "liris ${nombrigi(nombro.toBigInteger())} ${nombrigi(decimalo)}"
            }
        }
    }

    private fun troviPrefikson(nombro: BigInteger): Pair<BigInteger, String?> {
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
                return restantaj * 10.toBigInteger().pow(nuloj - it.key) to it.value
            }
        }
        return nombro to null
    }
}