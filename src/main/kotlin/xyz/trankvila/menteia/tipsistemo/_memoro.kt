package xyz.trankvila.menteia.tipsistemo

import xyz.trankvila.menteia.memoro.Memoro
import kotlin.reflect.full.primaryConstructor

class paranas: timis() {
    override val _tipo = Memoro.lastaValuo?._tipo ?: throw MenteiaTipEkcepcio(klos(sindis(this)))

    override suspend fun _valuigi(): Any? {
        return Memoro.lastaValuo?._valuigi()
    }

    override suspend fun _simpligi(): timis? {
        return Memoro.lastaValuo
    }
}

class pamiras(val _opcio: timis): timis(_opcio) {
    override val _tipo = Memoro.lastaValuo?._tipo ?: throw MenteiaTipEkcepcio(klos(sindis(paranas())))

    override suspend fun _valuigi(): timis {
        val lastaValuo = Memoro.lastaValuo ?: throw MenteiaTipEkcepcio(klos(sindis(paranas())))
        val klaso = lastaValuo::class.primaryConstructor!!
        return when (klaso.parameters.size) {
            1 -> klaso.call(_opcio)
            2 -> klaso.call(_opcio, lastaValuo.ponem)
            3 -> klaso.call(_opcio, lastaValuo.ponem, lastaValuo.forem)
            else -> throw Exception("${lastaValuo::class} bezonas ${klaso.parameters.size} opciojn?!")
        }
    }

    override suspend fun _simpligi(): timis? {
        return _valuigi()._simpligi()
    }
}

class paponas(val _opcio: timis): timis(_opcio) {
    override val _tipo = Memoro.lastaValuo?._tipo ?: throw MenteiaTipEkcepcio(klos(sindis(paranas())))

    override suspend fun _valuigi(): timis {
        val lastaValuo = Memoro.lastaValuo ?: throw MenteiaTipEkcepcio(klos(sindis(paranas())))
        val klaso = lastaValuo::class.primaryConstructor!!
        return when (klaso.parameters.size) {
            2 -> klaso.call(lastaValuo.morem, _opcio)
            3 -> klaso.call(lastaValuo.morem, _opcio, lastaValuo.forem)
            else -> throw Exception("${lastaValuo::class} bezonas ${klaso.parameters.size} opciojn?!")
        }
    }

    override suspend fun _simpligi(): timis? {
        return _valuigi()._simpligi()
    }
}