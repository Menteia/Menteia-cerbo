package xyz.trankvila.menteia.tipsistemo

abstract class brodimis<morum: timis>(
        val _listo: List<morum>,
        morem: Any? = null,
        ponem: Any? = null,
        forem: Any? = null
) : timis(morem, ponem, forem) {
    override suspend fun _valuigi(): List<morum> {
        return _listo
    }
}

class brotas<morum: timis>(morem: morum, ponem: brodimis<morum>) : brodimis<morum>(
        listOf(morem) + ponem._listo,
        morem,
        ponem
)

class prenis<morum: timis> : brodimis<morum> (
        listOf()
)