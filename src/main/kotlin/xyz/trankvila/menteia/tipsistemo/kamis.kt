package xyz.trankvila.menteia.tipsistemo


abstract class kamis(
        val _ciferoj: List<Int>,
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): girimis(1, _ciferoj, morem, ponem, forem)

class mira : kamis(listOf(0))

class pona : kamis(listOf(1))

class fora : kamis(listOf(2))

class nona : kamis(listOf(3))

class tera : kamis(listOf(4))

class sina : kamis(listOf(5))

class lira : kamis(listOf(6))

class ŝona : kamis(listOf(7))

class kera : kamis(listOf(8))

class gina : kamis(listOf(9))

class miri(morem: kamis): kamis(listOf(0) + morem._ciferoj, morem)

class poni(morem: kamis): kamis(listOf(1) + morem._ciferoj, morem)

class fori(morem: kamis): kamis(listOf(2) + morem._ciferoj, morem)

class noni(morem: kamis): kamis(listOf(3) + morem._ciferoj, morem)

class teri(morem: kamis): kamis(listOf(4) + morem._ciferoj, morem)

class sini(morem: kamis): kamis(listOf(5) + morem._ciferoj, morem)

class liri(morem: kamis): kamis(listOf(6) + morem._ciferoj, morem)

class ŝoni(morem: kamis): kamis(listOf(7) + morem._ciferoj, morem)

class keri(morem: kamis): kamis(listOf(8) + morem._ciferoj, morem)

class gini(morem: kamis): kamis(listOf(9) + morem._ciferoj, morem)