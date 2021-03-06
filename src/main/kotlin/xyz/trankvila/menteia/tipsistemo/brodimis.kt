package xyz.trankvila.menteia.tipsistemo

abstract class brodimis<morum: timis>(
        val _listo: List<morum>,
        morem: renas? = null,
        ponem: renas? = null,
        forem: renas? = null
) : timis(morem, ponem, forem) {
    override suspend fun _valuigi(): List<morum> {
        return _listo
    }
}

class brotas<morum: timis>(morem: morum, ponem: brodimis<morum>) : brodimis<morum>(
        listOf(morem) + ponem._listo,
        morem,
        ponem
) {
    companion object {
        fun <morum: timis> igiListon(listo: List<morum>): brodimis<morum> {
            return listo.foldRight(prenis()) { valuo, acc ->
                brotas(valuo, acc)
            }
        }
    }
}

class prenis<morum: timis> : brodimis<morum> (
        listOf()
)