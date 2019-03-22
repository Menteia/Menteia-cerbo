package xyz.trankvila.menteia.vorttrakto

import xyz.trankvila.menteia.memoro.lokajObjektoj
import xyz.trankvila.menteia.tipsistemo.*
import xyz.trankvila.menteia.tipsistemo.interna._kreebla
import xyz.trankvila.menteia.tipsistemo.interna._nomitaAĵo
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

val lokajKlasoj = setOf(sanimis::class)

object Legilo {
    fun legi(frazo: String, vortoj: Iterator<String> = frazo.splitToSequence(" ").iterator()): timis {
        val sekva = vortoj.next()
        try {
            val tipo = Class.forName("xyz.trankvila.menteia.tipsistemo.$sekva").kotlin
            when {
                tipo.isAbstract -> TODO()
                tipo.isSubclassOf(_kreebla::class) -> {
                    return remis(tipo as KClass<out timis>)
                }
                else -> {
                    val bezonataj = tipo.primaryConstructor!!.parameters
                    val opcioj = bezonataj.map {
                        legi(frazo, vortoj)
                    }
                    opcioj.forEachIndexed { index, opcio ->
                        val bezonata = bezonataj[index].type.jvmErasure
                        if (!bezonata.isInstance(opcio)) {
                            throw MenteiaTipEkcepcio(pegi(klos(tres(opcio, remis(bezonata as KClass<out timis>)))))
                        }
                    }
                    return tipo.primaryConstructor!!.call(*opcioj.toTypedArray()) as timis
                }
            }
        } catch (e: ClassNotFoundException) {
            val vorto = Vortaro.alporti()[sekva]
            return if (vorto != null) {
                val klaso = tipoj.getValue(vorto.tipo)
                if (lokajKlasoj.contains(klaso)) {
                    return lokajObjektoj.getValue(vorto.vorto)
                }
                klaso.primaryConstructor!!.call(sekva)
            } else {
                _nomitaAĵo(sekva)
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