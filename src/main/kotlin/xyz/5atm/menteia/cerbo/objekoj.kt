package xyz.`5atm`.menteia.cerbo

import xyz.`5atm`.menteia.vorttrakto.SintaksoArbo

interface Objekto {
    val nomo: String
    suspend operator fun invoke(eco: SintaksoArbo): String {
        throw Exception("Nekonita eco de $nomo: ${eco.radiko}")
    }

    suspend operator fun invoke(eco: SintaksoArbo, valuo: SintaksoArbo): String {
        throw Exception("Ne eblas agordi $eco de $nomo al $valuo")
    }
}

val objektoj = mapOf(
        "klisemi" to klisemi,
        "frodeni" to frodeni,
        "brinemi" to brinemi
)