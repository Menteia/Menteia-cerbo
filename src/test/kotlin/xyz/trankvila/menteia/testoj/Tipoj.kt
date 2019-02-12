package xyz.trankvila.menteia.testoj

import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import xyz.trankvila.menteia.vorttrakto.SintaksoArbo

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
object Tipoj {
    @Test
    fun Simplajtipoj() {
        val arboj = listOf(
                "pona",
                "klisemi",
                "kamis",
                "sitana"
        )
        val pravaj = listOf(
                "kamis",
                "kredimis",
                "girimis",
                "sinamis"
        )
        val rezultoj = arboj.map {
            SintaksoArbo.konstrui(it)
        }
        rezultoj.forEachIndexed { index, rezulto ->
            assertEquals(1, rezulto.tipo.size)
            assertEquals(pravaj[index], rezulto.tipo[0])
        }
    }

    @Test
    fun SimplajTipojKunAktantoj() {
        val arboj = listOf(
                "valima pona pona",
                "lemona sitana",
                "fitam pona",
                "ponega pona"
        )
        val pravaj = listOf(
                "ʃanamis",
                "sadimis lunimis nomis nevum nomis posetim",
                "karimis",
                "girimis"
        )
        val rezultoj = arboj.map {
            SintaksoArbo.konstrui(it)
        }
        rezultoj.forEachIndexed { index, arbo ->
            assertEquals(pravaj[index], arbo.tipo.joinToString(" "))
        }
    }

    @Test
    fun TipojKunGenerajAktantoj() {
        val testoj = listOf(
                "taris pona pona",
                "taris pona ponega pona"
        )
        val pravaj = listOf(
                "kamis",
                "girimis"
        )
        val rezultoj = testoj.map {
            SintaksoArbo.konstrui(it)
        }
        rezultoj.forEachIndexed { index, arbo ->
            assertEquals(1, arbo.tipo.size)
            assertEquals(pravaj[index], arbo.tipo[0])
        }
    }
}