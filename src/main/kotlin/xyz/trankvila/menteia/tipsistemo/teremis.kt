package xyz.trankvila.menteia.tipsistemo

import java.time.Duration

abstract class teremis(
        val _valuo: Duration,
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): timis(morem, ponem, forem) {
    override suspend fun _valuigi(): Duration {
        return _valuo
    }
}

class trinis(morem: girimis): teremis(
        Duration.ofSeconds(morem._valuo.numeratorAsLong),
        morem
)

class nires(morem: girimis): teremis(
        Duration.ofMinutes(morem._valuo.numeratorAsLong),
        morem
)

class gomos(morem: girimis): teremis(
        Duration.ofHours(morem._valuo.numeratorAsLong),
        morem
)