package xyz.trankvila.menteia

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import xyz.trankvila.menteia.memoro.Konversacio
import xyz.trankvila.menteia.memoro.Memoro
import xyz.trankvila.menteia.tipsistemo.timis

object Agordo {
    private val logger = LoggerFactory.getLogger(this::class.java)

    var sendiMesaĝon: (timis) -> Job = {
        GlobalScope.launch { logger.debug(it.toString()) }
    }
}

var SlackKonversacio = Konversacio()