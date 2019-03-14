package xyz.trankvila.menteia.testoj.tipsistemo

import kotlinx.coroutines.runBlocking
import org.apache.commons.math3.fraction.BigFraction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import xyz.trankvila.menteia.tipsistemo.*
import java.math.BigDecimal
import java.math.BigInteger

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
            assertEquals(pravaj[i], runBlocking { n._valuigi() })
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
            assertEquals(BigFraction(pravaj[index]), runBlocking { kamis._valuigi() })
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
            assertEquals(BigFraction(pravaj[index]), runBlocking { girimis._valuigi() })
        }
    }

    @Test
    fun aliaj() {
        runBlocking {
            assertEquals(generas(pona(), fora())._valuigi(), poneras(fora())._valuigi())
            assertEquals(liris(pona(), sina())._valuigi(), generas(nona(), fora())._valuigi())
            assertEquals(dreta(poni(mira()))._valuigi(), prena(pona())._valuigi())
            assertEquals(poneras(ponega(pona()))._valuigi(), glima(pona())._valuigi())
            assertEquals(BigFraction(-5), gulos(sina())._valuigi())
            assertEquals(ponega(poni(mira()))._valuigi(), poni(miri(miri(miri(mira()))))._simpligi()._valuigi())
            assertEquals(sini(liri(mira()))._valuigi(), lemis.ciferigi(BigInteger.valueOf(560))._valuigi())
            assertEquals(liris(fori(mira()), sina())._valuigi(), lemis.nombrigi(BigDecimal.valueOf(20.5), 1)._valuigi())
        }
    }

    @Test
    fun nomoj() {
        assertEquals("pona", pona().toString())
        assertEquals("ŝoni ŝona", ŝoni(ŝona()).toString())
    }

    @Test
    fun operacioj() {
        runBlocking {
            assertEquals(ponega(pona())._valuigi(), taris(teri(miri(mira())), liri(miri(mira())))._valuigi())
        }
    }
}