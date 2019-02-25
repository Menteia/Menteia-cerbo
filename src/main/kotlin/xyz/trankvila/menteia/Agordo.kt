package xyz.trankvila.menteia

import xyz.trankvila.menteia.tipsistemo.timis

object Agordo {
    val sendiMesaĝon = ThreadLocal<(timis) -> Unit>()
}