package xyz.trankvila.menteia

import kotlinx.coroutines.runBlocking
import xyz.trankvila.menteia.datumo.RealaAlirilaro
import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.tipsistemo.*
import xyz.trankvila.menteia.vorttrakto.Legilo
import java.time.LocalDate

fun main() {
    runBlocking {
        println(Legilo.legi("doni ko taris pona pona")._valuigi())
        println(Legilo.legi("doni ko taris paranas pona")._valuigi())
        println(Legilo.legi("doni ko paranas")._valuigi())
        println(Legilo.legi("doni ko paranas")._valuigi())
    }
}