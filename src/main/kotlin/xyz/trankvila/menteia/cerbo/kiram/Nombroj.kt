package xyz.trankvila.menteia.cerbo.kiram

import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.util.*

object Nombroj {
    private val nombroj = listOf("mora", "pona", "fora", "nora", "tega", "sira", "lina", "ʃona", "kera", "gina")
    private val partnombroj = listOf("mori", "poni", "fori", "nori", "tegi", "siri", "lini", "ʃoni", "keri", "gini")
    private val prefiksoj = mapOf(
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

    fun legiNombron(arbo: SintaksoArbo): BigDecimal {
        val prefikso = maligitajPrefiksoj[arbo.radiko]
        return if (prefikso != null) {
            (legiCiferon(arbo.opcioj[0]) * Math.pow(10.0, prefikso.toDouble())).toBigDecimal()
        } else {
            legiCiferon(arbo).toBigDecimal()
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

    fun nombrigi(nombro: BigDecimal, decimalciferoj: Int = 3): String {
        if (nombro < BigDecimal.ZERO) {
            return nombrigi(-nombro, decimalciferoj)
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

        if (prefiksoj.containsKey(nuloj)) {
            return restantaj to prefiksoj[nuloj]
        } else {
            return nombro to null
        }
    }
}