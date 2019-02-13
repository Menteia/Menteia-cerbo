package xyz.trankvila.menteia.testoj.tipsistemo

import org.apache.commons.math3.fraction.BigFraction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import xyz.trankvila.menteia.tipsistemo.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
object NombrojTesto {
    @Test
    fun simplatesto() {
        val vortoj = listOf(
                ŝona(),
                mira(),
                fora(),
                nona()
        )
        val pravaj = listOf(7, 0, 2, 3).map {
            BigFraction(it)
        }
        vortoj.forEachIndexed { i, n ->
            assertEquals(pravaj[i], n._valuigi())
        }
    }

    @Test
    fun nombrojKunPlurajCiferoj() {
        val vortoj = listOf(
                ŝoni(mira()),
                noni(ŝona()),
                poni(gina()),
                teri(sini(nona()))
        )
        val pravaj = listOf(70, 37, 19, 453)
        vortoj.forEachIndexed { index, kamis ->
            assertEquals(BigFraction(pravaj[index]), kamis._valuigi())
        }
    }

    @Test
    fun prefiksoj() {
        val vortoj = listOf(
                ponega(pona()),
                sariga(fori(tera()))
        )
        val pravaj = listOf(
                1000,
                24_000_000
        )
        vortoj.forEachIndexed { index, girimis ->
            assertEquals(BigFraction(pravaj[index]), girimis._valuigi())
        }
    }

    @Test
    fun aliaj() {
        assertEquals(generas(pona(), fora()), poneras(fora()))
        assertEquals(liris(pona(), sina()), generas(nona(), fora()))
        assertEquals(dreta(poni(mira())), prena(pona()))
        assertEquals(poneras(ponega(pona())), glima(pona()))
        assertEquals(BigFraction(-5), gulos(sina())._valuigi())
    }

    @Test
    fun nomoj() {
        assertEquals("pona", pona().toString())
        assertEquals("ŝoni ŝona", ŝoni(ŝona()).toString())
    }

    @Test
    fun operacioj() {
        assertEquals(ponega(pona()), taris(teri(miri(mira())), liri(miri(mira()))))
    }
}