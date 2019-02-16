package xyz.trankvila.menteia.tipsistemo

import org.apache.commons.math3.fraction.BigFraction

abstract class _unito(
        val _valuo: BigFraction,
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): timis(morem, ponem, forem) {
    override suspend fun _valuigi(): BigFraction {
        return _valuo
    }
}

abstract class `nomis nevum`(
        _valuo: BigFraction,
        morem: Any? = null
): _unito(_valuo, morem)

class nevum(morem: lemis): `nomis nevum`(morem._valuo, morem)