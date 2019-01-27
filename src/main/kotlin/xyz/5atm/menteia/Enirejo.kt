package xyz.`5atm`.menteia

import kotlinx.coroutines.runBlocking
import xyz.`5atm`.menteia.datumo.getThermostatAlt
import paroli
import xyz.`5atm`.menteia.cerbo.Cerbo
import xyz.`5atm`.menteia.datumo.forecast
import java.util.*
import xyz.`5atm`.menteia.datumo.getState

fun main(args: Array<String>) {
    runBlocking {
        var vico = readLine()
        while (vico != null) {
            val elirarbo = Cerbo.trakti(vico)
            paroli(elirarbo)
            println(elirarbo)
            vico = readLine()
        }
    }
}