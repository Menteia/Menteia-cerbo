package xyz.trankvila.menteia.tipsistemo

import org.apache.commons.math3.fraction.BigFraction

abstract class girimis(
        _faktoro: Int,
        _valuo: List<Int>,
        morem: timis? = null,
        ponem: timis? = null,
        forem: timis? = null
): lemis(
        BigFraction(_valuo.fold(0) { acc, i ->
            acc * 10 + i
        } * _faktoro),
        morem, ponem, forem
)

class ponega(morem: kamis): girimis(1000, morem._ciferoj, morem)

class sariga(morem: kamis): girimis(1_000_000, morem._ciferoj, morem)