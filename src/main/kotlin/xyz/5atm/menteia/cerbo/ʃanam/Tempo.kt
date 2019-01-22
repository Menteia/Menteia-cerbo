package xyz.`5atm`.menteia.cerbo.ʃanam

import xyz.`5atm`.menteia.cerbo.kiram.Nombroj
import java.util.*

object Tempo {
    private val fazoj = listOf("valima", "darena", "gemuna")

    fun geradas(): String {
        val nun = Calendar.getInstance()
        val fazo = fazoj[nun[Calendar.HOUR_OF_DAY] / 8]
        val horo = nun[Calendar.HOUR_OF_DAY] % 8
        val minuto = nun[Calendar.MINUTE]
        return "$fazo ${Nombroj.nombrigi(horo)} ${Nombroj.nombrigi(minuto)}"
    }
}