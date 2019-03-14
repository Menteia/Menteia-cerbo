package xyz.trankvila.menteia.tipsistemo

abstract class divimis<morum: timis, ponum: timis>(
        val _valuo1: morum,
        val _valuo2: ponum
): timis(_valuo1, _valuo2)

class divis<morum: timis, ponum: timis>(morem: morum, ponem: ponum): divimis<morum, ponum>(morem, ponem) {
    override suspend fun _valuigi(): Pair<morum, ponum> {
        return _valuo1 to _valuo2
    }
}