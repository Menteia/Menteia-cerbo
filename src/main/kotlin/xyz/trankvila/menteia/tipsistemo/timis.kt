package xyz.trankvila.menteia.tipsistemo

abstract class timis(
        val morem: timis? = null,
        val ponem: timis? = null,
        val forem: timis? = null
) {
    abstract fun _valuigi(): Any

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is timis -> {
                _valuigi() == other._valuigi()
            }
            else -> super.equals(other)
        }
    }

    override fun toString(): String {
        val nomo = mutableListOf(this::class.simpleName!!)
        if (morem != null) {
            nomo.add(morem.toString())
        }
        if (ponem != null) {
            nomo.add(ponem.toString())
        }
        if (forem != null) {
            nomo.add(forem.toString())
        }
        return nomo.joinToString(" ")
    }
}