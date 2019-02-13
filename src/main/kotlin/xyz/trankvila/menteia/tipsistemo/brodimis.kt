package xyz.trankvila.menteia.tipsistemo

abstract class brodimis<morum: timis>(
        val _listo: List<morum>,
        morem: timis? = null,
        ponem: timis? = null,
        forem: timis? = null
) : timis(morem, ponem, forem) {
    override fun _valuigi(): List<morum> {
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