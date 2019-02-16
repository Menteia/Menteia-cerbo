package xyz.trankvila.menteia.tipsistemo

abstract class sadimis<morum: timis, ponum: timis, forum: timis>(
        val _valuo1: morum, val _valuo2: ponum, val _valuo3: forum
): timis(_valuo1, _valuo2, _valuo3) {
    override suspend fun _valuigi(): Triple<morum, ponum, forum> {
        return Triple(_valuo1, _valuo2, _valuo3)
    }
}

class sadika<morum: timis, ponum: timis, forum: timis>(
        morem: morum, ponem: ponum, forem: forum
): sadimis<morum, ponum, forum>(morem, ponem, forem)