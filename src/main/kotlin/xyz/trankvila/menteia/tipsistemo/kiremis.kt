package xyz.trankvila.menteia.tipsistemo

import org.apache.commons.math3.fraction.BigFraction

abstract class kiremis(
        val _faktoro: Int,
        val _ciferoj: List<Int>,
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
) : lemis(
        BigFraction(
            _ciferoj.fold(0) { acc, i ->
                acc * 10 + i
            },
            _faktoro
    ), morem, ponem, forem
)

class prena(morem: kamis): kiremis(10, morem._ciferoj, morem)

class dreta(morem: kamis): kiremis(100, morem._ciferoj, morem)

class glima(morem: kamis): kiremis(1000, morem._ciferoj, morem)