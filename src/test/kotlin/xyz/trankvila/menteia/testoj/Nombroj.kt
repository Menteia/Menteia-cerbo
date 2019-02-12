package xyz.trankvila.menteia.testoj

import org.apache.commons.math3.fraction.BigFraction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import xyz.trankvila.menteia.cerbo.kiramis.Nombroj
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
object NombrajTestoj {
    @Test
    fun Entjeroj() {
        val nombroj = listOf(
                1_234_567_890,
                777,
                0,
                -7_777_777,
                -123,
                3000,
                2300
        )
        val rezultoj = nombroj.map {
            Nombroj.nombrigi(it)
        }
        val pravaj = listOf(
                "poni fori nori tegi siri lini ʃoni keri gini mira",
                "ʃoni ʃoni ʃona",
                "mira",
                "gulos ʃoni ʃoni ʃoni ʃoni ʃoni ʃoni ʃona",
                "gulos poni fori nora",
                "ponega nora",
                "fori nori miri mira"
        )
        pravaj.forEachIndexed { index, s ->
            assertEquals(s, rezultoj[index])
        }
    }

    @Test
    fun nombroj1() {
        val teksto = "dreta siri mira"
        val prava = BigFraction(1,2)
        assertEquals(prava, Nombroj.legiNombron(SintaksoArbo.konstrui(teksto)))
    }

    @Test
    fun nombroj2() {
        val nombro = BigDecimal("177.3")
        val prava = "liris poni ʃoni ʃona nora"
        assertEquals(prava, Nombroj.nombrigi(nombro, decimalciferoj = 1))
    }

    @Test
    fun frakcioj() {
        val teksto = "liris pona pona"
        val prava = BigFraction(11, 10)
        val rezulto = Nombroj.legiNombron(SintaksoArbo.konstrui(teksto))
        assertEquals(prava.numerator, rezulto.numerator)
        assertEquals(prava.denominator, rezulto.denominator)
    }
}