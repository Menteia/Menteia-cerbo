package xyz.trankvila.menteia.vorttrakto

import xyz.trankvila.menteia.datumo.alirilaro
import xyz.trankvila.menteia.memoro.Memoro
import xyz.trankvila.menteia.memoro.lokajObjektoj
import xyz.trankvila.menteia.tipsistemo.*
import kotlin.reflect.KClass
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
                tipo == des::class -> {
                    val objekto = legi(frazo, vortoj)
                    val eco = vortoj.next()
                    return des(objekto, eco)
                }
                tipo == miris::class -> {
                    val objekto = legi(frazo, vortoj)
                    val eco = vortoj.next()
                    val valuo = legi(frazo, vortoj)
                    return miris(objekto, eco, valuo)
                }
                tipo == marina::class -> {
                    val klaso = tipoj.getValue(vortoj.next())
                    return marina(klaso)
                }
                tipo == marisa::class -> {
                    val klaso = tipoj.getValue(vortoj.next())
                    val opcio = legi(frazo, vortoj)
                    return marisa(klaso, opcio)
                }
                tipo == paranas::class -> {
                    return Memoro.lastaValuo ?: throw MenteiaTipEkcepcio(pegi(klos(sindis(paranas()))))
                }
                else -> {
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