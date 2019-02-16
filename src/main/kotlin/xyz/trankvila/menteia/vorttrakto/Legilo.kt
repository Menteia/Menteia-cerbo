package xyz.trankvila.menteia.vorttrakto

import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.tipsistemo.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object Legilo {
    fun legi(frazo: String, vortoj: Iterator<String> = frazo.splitToSequence(" ").iterator()): timis {
        val sekva = vortoj.next()
        val tipo = xyz.trankvila.menteia.tipsistemo.vortoj[sekva]
        if (tipo == null) {
            val vorto = Vortaro.alporti().getValue(sekva)
            val klaso = tipoj.getValue(vorto.tipo)
            return klaso.primaryConstructor!!.call(sekva)
        } else {
            if (tipo.isAbstract) {
                TODO()
            } else if (tipo == des::class) {
                val objekto = legi(frazo, vortoj)
                val eco = vortoj.next()
                return des(objekto, eco)
            } else {
                val bezonataj = tipo.primaryConstructor!!.parameters
                val opcioj = bezonataj.map {
                    legi(frazo, vortoj)
                }
                opcioj.forEachIndexed { index, opcio ->
                    val bezonata = bezonataj[index].type.jvmErasure
                    if (!bezonata.isInstance(opcio)) {
                        throw MenteiaTipEkcepcio(pegi(klos(tres(opcio, bezonata))))
                    }
                }
                return tipo.primaryConstructor!!.call(*opcioj.toTypedArray())
            }
        }
    }

    fun _nomoKunTipaktantoj(_klaso: KClass<*>): String {
        if (_klaso.typeParameters.isEmpty()) {
            return _klaso.simpleName!!
        } else {
            return "${_klaso.simpleName!!} ${_klaso.typeParameters.map {
                it.name
            }.joinToString(" ")}"
        }
    }
}