package xyz.`5atm`.menteia.cerbo.gremuna

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.kiram.Nombroj

object Daŭro {
    fun legiDaŭron(daŭro: SintaksoArbo): Int {
        return when (daŭro.radiko) {
            "gomos" -> Nombroj.legiNombron(daŭro.opcioj[0]) * 3600
            "nires" -> Nombroj.legiNombron(daŭro.opcioj[0]) * 60
            "trimis" -> Nombroj.legiNombron(daŭro.opcioj[0])
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