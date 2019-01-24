package xyz.`5atm`.menteia.cerbo.kiram

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import java.util.*

object Nombroj {
    private val nombroj = listOf("mora", "pona", "fora", "nora", "tega", "sira", "lina", "ʃona", "kera", "gina")
    private val partnombroj = listOf("mori", "poni", "fori", "nori", "tegi", "siri", "lini", "ʃoni", "keri", "gini")

    fun nombrigi(nombro: Int): String {
        if (nombro < 0) {
            return "gulos ${nombrigi(-nombro)}"
        } else if (nombro < 10) {
            return nombroj[nombro]
        } else {
            val ciferoj = Stack<String>()
            var restanta = nombro
            while (restanta > 0) {
                val cifero = restanta % 10
                if (ciferoj.isEmpty()) {
                    ciferoj.push(nombroj[cifero])
                } else {
                    ciferoj.push(partnombroj[cifero])
                }
                restanta /= 10
            }
            val sb = StringBuilder()
            while (ciferoj.isNotEmpty()) {
                sb.append(ciferoj.pop())
                if (ciferoj.isNotEmpty()) {
                    sb.append(' ')
                }
            }
            return sb.toString()
        }
    }

    fun legiNombron(arbo: SintaksoArbo): Int {
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
}