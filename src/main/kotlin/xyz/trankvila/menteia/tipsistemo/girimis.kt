package xyz.trankvila.menteia.tipsistemo

import org.apache.commons.math3.fraction.BigFraction

abstract class girimis(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
): lemis(
        morem, ponem, forem
) {
    protected abstract val _faktoro: Int
    abstract val _ciferoj: List<Int>
    override val _valuo get() = BigFraction(_ciferoj.fold(0) { acc, i ->
            acc * 10 + i
        } * _faktoro)
}

class ponega(morem: kamis): girimis(morem) {
    override val _faktoro = 1000
    override val _ciferoj = morem._ciferoj
}

class sariga(morem: kamis): girimis(morem) {
    override val _faktoro = 1_000_000
    override val _ciferoj = morem._ciferoj
}