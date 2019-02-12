package xyz.trankvila.menteia.cerbo

import xyz.trankvila.menteia.cerbo.kiramis.Nombroj
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo

object Matematiko {
    fun taris(nombro1: SintaksoArbo, nombro2: SintaksoArbo): Pair<String, Certeco> {
        val n1 = Nombroj.legiNombron(nombro1)
        val n2 = Nombroj.legiNombron(nombro2)
        val rezulto = n1.add(n2)
        return Nombroj.nombrigi(rezulto) to Certeco.Negi
    }

    fun kredas(nombro1: SintaksoArbo, nombro2: SintaksoArbo): Pair<String, Certeco> {
        val n1 = Nombroj.legiNombron(nombro1)
        val n2 = Nombroj.legiNombron(nombro2)
        val rezulto = n1.multiply(n2)
        return Nombroj.nombrigi(rezulto) to Certeco.Negi
    }
}