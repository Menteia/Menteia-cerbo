package xyz.trankvila.menteia.tipsistemo

import kotlinx.coroutines.runBlocking
import xyz.trankvila.menteia.tipsistemo.interna._certeco
import java.time.LocalTime
import java.time.ZoneId

val timezone = ZoneId.of("America/Vancouver")

abstract class ŝanamis(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
): timis(morem, ponem, forem) {
    override val _tipo = _certeco.negi
    protected abstract val _horo: LocalTime

    companion object {
        private val fazoj = listOf(::valima, ::darena, ::gemuna)

        fun igi(tempo: LocalTime): ŝanamis {
            val fazo = fazoj[tempo.hour / 8]
            val horo = tempo.hour % 8
            val minuto = tempo.minute
            return fazo(lemis.ciferigi(horo.toBigInteger()), lemis.ciferigi(minuto.toBigInteger()))
        }
    }

    override suspend fun _valuigi(): LocalTime {
        return _horo
    }

    override suspend fun _simpligi(): timis {
        return igi(_horo)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is ŝanamis -> {
                return _horo == runBlocking { other._valuigi() }
            }
            else -> {
                super.equals(other)
            }
        }
    }
}

class geradas: ŝanamis() {
    override val _horo = LocalTime.now(timezone)
}

class valima(morem: kamis, ponem: kamis): ŝanamis(morem, ponem) {
    override val _horo = runBlocking {
        LocalTime.of(
                morem._valuigi().numeratorAsInt,
                ponem._valuigi().numeratorAsInt
        )
    }
}

class darena(morem: kamis, ponem: kamis): ŝanamis(morem, ponem) {
    override val _horo = runBlocking {
        LocalTime.of(
                morem._valuigi().numeratorAsInt + 8,
                ponem._valuigi().numeratorAsInt
        )
    }
}

class gemuna(morem: kamis, ponem: kamis): ŝanamis(morem, ponem) {
    override val _horo = runBlocking {
        LocalTime.of(
                morem._valuigi().numeratorAsInt + 16,
                ponem._valuigi().numeratorAsInt
        )
    }
}