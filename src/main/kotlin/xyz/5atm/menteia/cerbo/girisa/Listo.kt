package xyz.`5atm`.menteia.cerbo.girisa

import xyz.`5atm`.menteia.cerbo.Certeco
import xyz.`5atm`.menteia.cerbo.Iloj
import xyz.`5atm`.menteia.cerbo.NomitaAĵo
import alportiListon

class Listo(override val nomo: String) : NomitaAĵo {
    override fun priskribi(): Pair<String, Certeco> {
        val listo = alportiListon(nomo)
        return Iloj.listigi(listo) to Certeco.Negi
    }
}