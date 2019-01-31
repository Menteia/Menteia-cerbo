package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo
import xyz.`5atm`.menteia.cerbo.girisa.Listo
import Vortaro

interface NomitaAĵo {
    val nomo: String
    suspend operator fun invoke(eco: SintaksoArbo): Pair<String, Certeco> {
        throw Exception("Nekonita eco de $nomo: ${eco.radiko}")
    }

    suspend operator fun invoke(eco: SintaksoArbo, valuo: SintaksoArbo): String {
        throw Exception("Ne eblas agordi $eco de $nomo al $valuo")
    }

    fun priskribi(): Pair<String, Certeco> {
        return nomo to Certeco.Negi
    }
}

fun troviNomitanAĵon(nomo: SintaksoArbo): NomitaAĵo {
    val vorto = Vortaro.alporti()[nomo.radiko] ?: throw Exception("Ne eblas trovi ${nomo.radiko}")
    return objektoj[vorto.vorto] ?:
            when (vorto.tipo) {
                "girisa" -> Listo(vorto.vorto)
                else -> throw Exception("${vorto.vorto} ne havas konitan tipon")
            }
}

val objektoj = mapOf(
        "klisemi" to klisemi,
        "frodeni" to frodeni,
        "brinemi" to brinemi,

        "girisa" to Listo
)