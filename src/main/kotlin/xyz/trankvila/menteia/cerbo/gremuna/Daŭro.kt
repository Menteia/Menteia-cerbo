package xyz.trankvila.menteia.cerbo.gremuna

import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.kiram.Nombroj

object Daŭro {
    fun legiDaŭron(daŭro: SintaksoArbo): Long {
        return when (daŭro.radiko) {
            "gomos" -> Nombroj.legiEntjeron(daŭro.opcioj[0]).toLong() * 3600
            "nires" -> Nombroj.legiEntjeron(daŭro.opcioj[0]).toLong() * 60
            "trimis" -> Nombroj.legiEntjeron(daŭro.opcioj[0]).toLong()
            else -> throw Exception("$daŭro ne estas valida daŭro")
        }
    }

    fun priskribiDaŭron(sekondoj: Int): String {
        if (sekondoj < 60) {
            return "trimis ${Nombroj.nombrigi(sekondoj)}"
        } else if (sekondoj < 3600) {
            val minutoj = sekondoj / 60
            val restantaj = sekondoj % 60
            if (restantaj == 0) {
                return "nires ${Nombroj.nombrigi(minutoj)}"
            } else {
                return "tamis nires ${Nombroj.nombrigi(minutoj)} trimis ${Nombroj.nombrigi(sekondoj)}"
            }
        } else {
            val horoj = sekondoj / 3600
            val minutoj = sekondoj / 60 % 60
            val restantaj = sekondoj % 60
            val valoroj = mutableListOf("gomos ${Nombroj.nombrigi(horoj)}")
            if (minutoj > 0) {
                valoroj.add("nires ${Nombroj.nombrigi(minutoj)}")
            }
            if (restantaj > 0) {
                valoroj.add("trimis ${Nombroj.nombrigi(restantaj)}")
            }
            return when (valoroj.size) {
                1 -> valoroj.first()
                2 -> "trimis ${valoroj[0]} ${valoroj[1]}"
                else -> "gonitis ${Nombroj.nombrigi(horoj)} ${Nombroj.nombrigi(minutoj)} ${Nombroj.nombrigi(restantaj)}"
            }
        }
    }
}