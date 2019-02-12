package xyz.trankvila.menteia.cerbo

import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import xyz.trankvila.menteia.cerbo.brodimis.Listo
import xyz.trankvila.menteia.cerbo.sanimis.Tempoŝaltilo
import Vortaro
import xyz.trankvila.menteia.cerbo.timis.Tipsistemo

interface NomitaAĵo {
    val nomo: String
    suspend operator fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        throw MenteiaEksepcio("klos tinas des ${nomo} ${eco}")
    }

    suspend operator fun invoke(eco: SintaksoArbo, valuo: SintaksoArbo): Pair<String, Certeco> {
        throw Exception("Ne eblas agordi $eco de $nomo al $valuo")
    }

    fun priskribi(): Pair<String, Certeco> {
        return nomo to Certeco.Negi
    }
}

fun troviNomitanAĵon(nomo: SintaksoArbo): NomitaAĵo {
    val vorto = Vortaro.alporti()[nomo.radiko] ?: throw Exception("Ne eblas trovi ${nomo.radiko}")
    val objekto = objektoj[vorto.vorto]
    if (objekto != null) {
        return objekto
    }
    val tipo = Tipsistemo.legiTipon(vorto.tipo!!)
    return when (tipo[0]) {
        "brodimis" -> Listo(vorto.vorto)
        "sanimis" -> Tempoŝaltilo(vorto.vorto)
        else -> throw Exception("${vorto.vorto} ne havas konitan tipon")
    }
}

val objektoj = mapOf(
        "klisemi" to klisemi,
        "frodeni" to frodeni,
        "brinemi" to brinemi,
        "minero" to minero,
        "namida" to namida,

        "brodimis" to Listo
)