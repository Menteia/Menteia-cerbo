package xyz.trankvila.menteia.tipsistemo

abstract class lunimis(
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
): timis(morem, ponem, forem) {
    override suspend fun _valuigi(): String {
        return this::class.simpleName!!
    }
}

class ŝanare: lunimis()
class molaro: lunimis()
class varumi: lunimis()
class saleri: lunimis()
class sakini: lunimis()
class furema: lunimis()
class salena: lunimis()
class plemuna: lunimis()
class plesuta: lunimis()
class murena: lunimis()
class plikreda: lunimis()
class salekres: lunimis()
class sikreri: lunimis()
class busmisa: lunimis()
class saremi: lunimis()
class klitepa: lunimis()
class ferisa: lunimis()
class krispita: lunimis()
class gletami: lunimis()
class lisa(morem: lunimis): lunimis(morem)
class mino(morem: lunimis): lunimis(morem)
class neda(morem: lunimis): lunimis(morem)
class trisa(morem: lunimis): lunimis(morem)
class kitri(morem: lunimis): lunimis(morem)
class saleni(morem: lunimis): lunimis(morem)
class pabupi(morem: lunimis): lunimis(morem)
class druva(morem: lunimis): lunimis(morem)