package dev.fobo66.crypto

import kotlin.test.Test
import kotlin.test.assertNotEquals

class RandomNumberGeneratorTest {
    @Test
    fun `random numbers are different`() {
        val generator = PseudoRandomNumberGenerator(0)
        val firstNumber = generator.computeLCG()
        val secondNumber = generator.computeLCG()
        assertNotEquals(firstNumber, secondNumber)
    }

    @Test
    fun `seed affects random numbers`() {
        val generator = PseudoRandomNumberGenerator(0)
        val generator2 = PseudoRandomNumberGenerator(42)
        val firstNumber = generator.computeLCG()
        val secondNumber = generator2.computeLCG()
        assertNotEquals(firstNumber, secondNumber)
    }

    @Test
    fun `random numbers ion different modes are different`() {
        val generator = PseudoRandomNumberGenerator(0)
        val firstNumber = generator.computeLCG()
        val secondNumber = generator.computeQuadraticLCG()
        assertNotEquals(firstNumber, secondNumber)
    }
}
