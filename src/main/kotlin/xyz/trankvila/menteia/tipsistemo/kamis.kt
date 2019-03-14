package xyz.trankvila.menteia.tipsistemo


abstract class kamis(
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
): girimis(morem, ponem, forem) {
    override val _faktoro = 1
}

class mira : kamis() {
    override val _ciferoj = listOf(0)
}

class pona : kamis() {
    override val _ciferoj = listOf(1)
}

class fora : kamis() {
    override val _ciferoj = listOf(2)
}

class nona : kamis() {
    override val _ciferoj = listOf(3)
}

class tera : kamis() {
    override val _ciferoj = listOf(4)
}

class sina : kamis() {
    override val _ciferoj = listOf(5)
}

class lira : kamis() {
    override val _ciferoj = listOf(6)
}

class ŝona : kamis() {
    override val _ciferoj = listOf(7)
}

class kera : kamis() {
    override val _ciferoj = listOf(8)
}

class gina : kamis() {
    override val _ciferoj = listOf(9)
}

class miri(morem: kamis): kamis(morem) {
    override val _ciferoj: List<Int> = listOf(0) + morem._ciferoj
}

class poni(morem: kamis): kamis(morem) {
    override val _ciferoj: List<Int> = listOf(1) + morem._ciferoj
}

class fori(morem: kamis): kamis(morem) {
    override val _ciferoj: List<Int> = listOf(2) + morem._ciferoj
}

class noni(morem: kamis): kamis(morem) {
    override val _ciferoj: List<Int> = listOf(3) + morem._ciferoj
}

class teri(morem: kamis): kamis(morem) {
    override val _ciferoj: List<Int> = listOf(4) + morem._ciferoj
}

class sini(morem: kamis): kamis(morem) {
    override val _ciferoj: List<Int> = listOf(5) + morem._ciferoj
}

class liri(morem: kamis): kamis(morem) {
    override val _ciferoj: List<Int> = listOf(6) + morem._ciferoj
}

class ŝoni(morem: kamis): kamis(morem) {
    override val _ciferoj: List<Int> = listOf(7) + morem._ciferoj
}

class keri(morem: kamis): kamis(morem) {
    override val _ciferoj: List<Int> = listOf(8) + morem._ciferoj
}

class gini(morem: kamis): kamis(morem) {
    override val _ciferoj: List<Int> = listOf(9) + morem._ciferoj
}