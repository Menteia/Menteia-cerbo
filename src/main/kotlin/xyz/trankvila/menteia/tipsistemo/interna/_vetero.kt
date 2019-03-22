package xyz.trankvila.menteia.tipsistemo.interna

import kotlinx.coroutines.Deferred
import xyz.trankvila.menteia.tipsistemo.renas
import xyz.trankvila.menteia.tipsistemo.timis
import java.time.LocalDate

abstract class _vetero(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
): timis(morem, ponem, forem) {
    override val _tipo = _certeco.sagi
    protected abstract val _loko: Deferred<Pair<String, String>>
    protected abstract val _dato: LocalDate?
}