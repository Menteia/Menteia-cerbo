package xyz.trankvila.menteia

import kotlinx.coroutines.runBlocking
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.tipsistemo.*
import xyz.trankvila.menteia.vorttrakto.Legilo
import java.time.LocalDate

fun main() {
    runBlocking {
        println(doni(ko(des(kredimis("klisemi"), "testos")))._valuigi())
    }
}