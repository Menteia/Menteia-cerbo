package xyz.trankvila.menteia

import kotlinx.coroutines.CoroutineScope
import xyz.trankvila.menteia.memoro.Konversacio
import xyz.trankvila.menteia.memoro.Memoro
import xyz.trankvila.menteia.tipsistemo.timis

object Agordo {
    val sendiMesaĝon = ThreadLocal<(timis) -> Unit>()
    val konteksto = ThreadLocal<CoroutineScope>()
}

var SlackKonversacio = Konversacio()