package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import java.util.*

object Iloj {
    fun listigi(listo: List<String>): String {
        if (listo.isEmpty()) {
            return "premis"
        } else if (listo.size == 1) {
            return listo.first()
        } else {
            return "brotas ${listo.joinToString(" brotas ")} premis"
        }
    }

    fun legiDaton(arbo: SintaksoArbo): Calendar {
        return when (arbo.radiko) {
            "fitam" -> {
                val dato = Calendar.getInstance()
                val nombro = Nombroj.legiNombron(arbo.opcioj[0])
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
}