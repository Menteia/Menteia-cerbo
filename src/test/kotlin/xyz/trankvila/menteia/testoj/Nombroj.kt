package xyz.trankvila.menteia.testoj

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import xyz.trankvila.menteia.cerbo.kiram.Nombroj
import xyz.trankvila.menteia.datumo.alirilaro

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
                "poni fori nori tegi siri lini ʃoni keri gini mora",
                "ʃoni ʃoni ʃona",
                "mora",
                "gulos ʃoni ʃoni ʃoni ʃoni ʃoni ʃoni ʃona",
                "gulos poni fori nora",
                "ponega nora",
                "fori nori mori mora"
        )
        pravaj.forEachIndexed { index, s ->
            assertEquals(s, rezultoj[index])
        }
    }
}