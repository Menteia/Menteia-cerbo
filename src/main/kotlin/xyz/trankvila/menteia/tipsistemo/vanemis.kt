package xyz.trankvila.menteia.tipsistemo

sealed class vanemis(
        morem: timis? = null,
        ponem: timis? = null,
        forem: timis? = null
): timis(morem, ponem, forem) {
    abstract class tadumis<morom: timis>(
            val _valuo: Boolean,
            val _frazo: morom,
            ponem: timis? = null,
            forem: timis? = null
    ): vanemis(_frazo, ponem, forem) {
        override fun _valuigi(): Boolean {
            return _valuo
        }
    }

    abstract class fragemis<morum: timis>(
            val _valuo: morum,
            morem: timis? = null,
            ponem: timis? = null,
            forem: timis? = null
    ): vanemis(morem, ponem, forem)
}

class to(morem: timis, ponem: timis): vanemis.tadumis<timis>(
        morem == ponem,
        morem, ponem
)

class ko<morum: timis>(morem: morum): vanemis.fragemis<morum>(
        morem,
        morem
) {
    override fun _valuigi(): Any {
        return _valuo._valuigi()
    }
}

class klos(morem: vanemis.tadumis<*>): vanemis.tadumis<timis>(
        !morem._valuo,
        morem
)