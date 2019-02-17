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

abstract class `nomis meforam`(
        _valuo: BigFraction,
        morem: Any? = null
): _unito(_valuo, morem)

class meforam(morem: lemis): `nomis meforam`(morem._valuo, morem)

abstract class `nomis perom`(
        _valuo: BigFraction,
        morem: Any? = null
): _unito(_valuo, morem)

class perom(morem: lemis): `nomis perom`(morem._valuo, morem)

abstract class `nomis balim`(
        _valuo: BigFraction,
        morem: Any? = null
): _unito(_valuo, morem)

class balim(morem: lemis): `nomis balim`(morem._valuo, morem)

abstract class `nomis senam`(
        _valuo: BigFraction,
        morem: Any? = null
): _unito(_valuo, morem)

class senam(morem: lemis): `nomis senam`(morem._valuo, morem)