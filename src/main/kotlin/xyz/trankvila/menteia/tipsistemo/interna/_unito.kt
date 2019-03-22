package xyz.trankvila.menteia.tipsistemo.interna

import org.apache.commons.math3.fraction.BigFraction
import xyz.trankvila.menteia.tipsistemo.lemis
import xyz.trankvila.menteia.tipsistemo.timis

abstract class _unito(val _valuo: lemis): timis(_valuo) {
    override suspend fun _valuigi(): BigFraction {
        return _valuo._valuigi()
    }
}