package xyz.trankvila.menteia.tipsistemo.interna

import xyz.trankvila.menteia.tipsistemo.renas
import xyz.trankvila.menteia.tipsistemo.timis

abstract class _modo(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
) : timis(morem, ponem, forem) {
    override suspend fun _valuigi(): _modo {
        return this
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is _modo -> {
                this::class == other::class
            }
            else -> super.equals(other)
        }
    }
}