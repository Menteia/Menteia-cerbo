package xyz.trankvila.menteia

object Agordo {
    val kunuloID = ThreadLocal<String>()
    val ĉuSendiAlAlirilo = ThreadLocal.withInitial { false }
    val poŝtelefonnumero = ThreadLocal<String>()
}