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
    fun `random numbers ion different modes are different`() {
        val generator = PseudoRandomNumberGenerator(0)
        val firstNumber = generator.computeLCG()
        val secondNumber = generator.computeQuadraticLCG()
        assertNotEquals(firstNumber, secondNumber)
    }
}
