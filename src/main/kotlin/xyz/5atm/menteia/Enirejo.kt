package xyz.`5atm`.menteia

import kotlinx.coroutines.runBlocking
import paroli
import xyz.`5atm`.menteia.cerbo.Cerbo

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