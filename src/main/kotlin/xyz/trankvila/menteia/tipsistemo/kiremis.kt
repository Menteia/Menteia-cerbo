package xyz.trankvila.menteia.tipsistemo

import org.apache.commons.math3.fraction.BigFraction

abstract class kiremis(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
) : lemis(morem, ponem, forem) {
    protected abstract val _faktoro: Int
    protected abstract val _ciferoj: List<Int>

    override val _valuo get() = BigFraction(
            _ciferoj.fold(0) { acc, i -> acc * 10 + i }, _faktoro
    )
}

class prena(morem: kamis): kiremis(morem) {
    override val _faktoro = 10
    override val _ciferoj = morem._ciferoj
}

class dreta(morem: kamis): kiremis( morem) {
    override val _faktoro = 100
    override val _ciferoj = morem._ciferoj
}

class glima(morem: kamis): kiremis(morem) {
    override val _faktoro = 1000
    override val _ciferoj = morem._ciferoj
}