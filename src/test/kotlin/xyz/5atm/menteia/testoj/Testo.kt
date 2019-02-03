package xyz.`5atm`.menteia.testoj

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import xyz.`5atm`.menteia.datumo.alirilaro

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
object MenteiaTestoj {
    @BeforeAll
    fun algordiAlirilaron() {
        println("123")
    }

    @Test
    fun testaTesto() {
        assertEquals(1 + 1, 2)
    }
}