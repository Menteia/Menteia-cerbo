package xyz.`5atm`.menteia

import paroli
import xyz.`5atm`.menteia.cerbo.Cerbo

fun main(args: Array<String>) {
    var vico = readLine()
    while (vico != null) {
        val elirarbo = Cerbo.trakti(vico)
        paroli(elirarbo)
        println(elirarbo)
        vico = readLine()
    }
}