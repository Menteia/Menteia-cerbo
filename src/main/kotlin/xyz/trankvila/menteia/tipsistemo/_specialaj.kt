package xyz.trankvila.menteia.tipsistemo

import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.declaredMemberFunctions

class des(val _objekto: timis, val _eco: String): timis(
        _objekto, _eco
) {
    override suspend fun _valuigi(): timis? {
        return _simpligi()
    }

    override suspend fun _simpligi(): timis? {
        val eco = _objekto::class.declaredMemberFunctions.find {
            it.name == _eco
        } ?: throw MenteiaTipEkcepcio(pegi(klos(sindis(this))))
        return eco.callSuspend(_objekto) as timis?
    }
}