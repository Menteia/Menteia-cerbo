package xyz.`5atm`.menteia.cerbo

object Iloj {
    fun listigi(listo: List<String>): String {
        if (listo.isEmpty()) {
            return "premis"
        } else if (listo.size == 1) {
            return listo.first()
        } else {
            return "brotas ${listo.joinToString(" brotas ")} premis"
        }
    }
}