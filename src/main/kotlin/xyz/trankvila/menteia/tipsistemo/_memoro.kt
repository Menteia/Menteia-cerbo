package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.memoro.Memoro

class paranas: timis() {
    override suspend fun _valuigi(): timis? {
        return Memoro.lastaValuo
    }

    override suspend fun _simpligi(): timis? {
        return Memoro.lastaValuo
    }
}