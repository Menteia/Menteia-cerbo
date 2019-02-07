package xyz.trankvila.menteia.cerbo

import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.kiram.Nombroj
import java.util.*

object Iloj {
    fun listigi(listo: List<String>): String {
        return when (listo.size) {
            0 -> "premis"
            1 -> listo.first()
            2 -> "divis ${listo[0]} ${listo[1]}"
            3 -> "sadika ${listo[0]} ${listo[1]} ${listo[2]}"
            else -> "brotas ${listo.joinToString(" brotas ")} premis"
        }
    }

    fun arigi(aro: Map<String, List<String>>): String {
        if (aro.isEmpty()) {
            return "premis"
        } else {
            return aro.map {
                "pretas ${it.key} ${listigi(it.value)}"
            }.joinToString(" ", postfix = " premis")
        }
    }

    fun legiDaton(arbo: SintaksoArbo): Calendar {
        return when (arbo.radiko) {
            "fitam" -> {
                val dato = Calendar.getInstance()
                val nombro = Nombroj.legiEntjeron(arbo.opcioj[0]).toInt()
                dato.add(Calendar.DATE, nombro)
                dato.set(Calendar.HOUR_OF_DAY, 0)
                dato.set(Calendar.MILLISECOND, 0)
                dato.set(Calendar.MINUTE, 0)
                dato.set(Calendar.SECOND, 0)
                dato
            }
            else -> throw Exception("Ne povas trakti ${arbo}")
        }
    }

    fun legiTemperaturon(arbo: SintaksoArbo): Int {
        return when (arbo.radiko) {
            "nevum" -> Nombroj.legiEntjeron(arbo.opcioj[0])
            else -> throw Exception("$arbo ne estas valida temperaturo")
        }
    }
}