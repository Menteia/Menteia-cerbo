package xyz.trankvila.menteia

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import xyz.trankvila.menteia.memoro.Konversacio
import xyz.trankvila.menteia.memoro.Memoro
import xyz.trankvila.menteia.tipsistemo.timis

object Agordo {
    var sendiMesaĝon: (timis) -> Job = {
        GlobalScope.launch { println(it.toString()) }
    }
}

var SlackKonversacio = Konversacio()