package xyz.trankvila.menteia.vorttrakto

import xyz.trankvila.menteia.tipsistemo.timis
import xyz.trankvila.menteia.tipsistemo.tipoj
import kotlin.reflect.full.primaryConstructor

object Legilo {
    fun legi(frazo: String, vortoj: Iterator<String> = frazo.splitToSequence(" ").iterator()): timis {
        val sekva = vortoj.next()
        val tipo = tipoj.getValue(sekva)
        val opcioj = (1..tipo.primaryConstructor!!.parameters.size).map {
            legi(frazo, vortoj)
        }
        return tipo.primaryConstructor!!.call(*opcioj.toTypedArray())
    }
}