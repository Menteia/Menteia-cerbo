package xyz.`5atm`.menteia.vorttrakto

internal data class SintaksoArbo(val radiko: String, val opcioj: List<SintaksoArbo>) {
    fun traversi(kunPaŭzoj: Boolean = false): Sequence<String> {
        return sequence {
            yield(radiko)
            opcioj.forEach {
                yieldAll(it.traversi())
            }
        }
    }
}

internal fun silaboj(vorto: String): List<String> {

}