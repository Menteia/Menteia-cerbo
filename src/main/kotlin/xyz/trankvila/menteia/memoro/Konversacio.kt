package xyz.trankvila.menteia.memoro

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import xyz.trankvila.menteia.Agordo
import xyz.trankvila.menteia.tipsistemo.*
import xyz.trankvila.menteia.tipsistemo.interna._kreebla
import xyz.trankvila.menteia.tipsistemo.interna._nomitaAĵo
import xyz.trankvila.menteia.vorttrakto.lokajKlasoj
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class Konversacio() {
    private val kanalo = Channel<String>()
    private val sekvaBezonata = Channel<KClass<out timis>?>()
    private val kompletaFrazo = GlobalScope.async {
        legi()
    }

    @ExperimentalCoroutinesApi
    suspend fun eniri(frazo: String): KClass<out timis>? {
        kanalo.send(frazo)
        return select {
            sekvaBezonata.onReceive {
                it
            }
            kompletaFrazo.onAwait {
                kanalo.close()
                sekvaBezonata.close()
                null
            }
        }
    }

    suspend fun fini(): timis {
        return kompletaFrazo.await()
    }

    private suspend fun legi(bezonataTipo: KClass<out timis>? = null): timis {
        if (bezonataTipo != null) {
            sekvaBezonata.send(bezonataTipo)
        }
        val radiko = kanalo.receive()
        try {
            val klaso = Class.forName("xyz.trankvila.menteia.tipsistemo.$radiko").kotlin
            return when {
                klaso.isAbstract -> TODO()
                klaso.isSubclassOf(_kreebla::class) -> {
                    remis(klaso as KClass<out timis>)
                }
                else -> {
                    val bezonataj = klaso.primaryConstructor!!.parameters
                    val opcioj = bezonataj.map {
                        legi(it.type.jvmErasure as KClass<out timis>)
                    }
                    opcioj.forEachIndexed { index, opcio ->
                        val bezonata = bezonataj[index].type.jvmErasure
                        if (!bezonata.isInstance(opcio)) {
                            throw MenteiaTipEkcepcio(pegi(klos(tres(opcio, remis(bezonata as KClass<out timis>)))))
                        }
                    }
                    klaso.primaryConstructor!!.call(*opcioj.toTypedArray()) as timis
                }
            }
        } catch (e: ClassNotFoundException) {
            val vorto = Vortaro.alporti()[radiko]
            return if (vorto != null) {
                val klaso = tipoj.getValue(vorto.tipo)
                if (lokajKlasoj.contains(klaso)) {
                    return lokajObjektoj.getValue(vorto.vorto)
                }
                klaso.primaryConstructor!!.call(radiko)
            } else {
                _nomitaAĵo(radiko)
            }
        } catch (e: InvocationTargetException) {
            e.targetException.printStackTrace()
            throw e.targetException
        }
    }
}