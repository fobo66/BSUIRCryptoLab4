/*

    LCG based on DES CTR mode

    Originally created by Iris Yuan as LCG implementation for learning purposes based on
    http://en.wikipedia.org/wiki/Linear_congruential_generator
    3/6/2014
*/
package dev.fobo66.crypto

import org.apache.commons.rng.simple.RandomSource

import java.nio.ByteBuffer
import kotlin.math.abs

class PseudoRandomNumberGenerator(seed: Int = 0) {

    // used to generate parameters of congruential generators within modulus' range
    private val rand = RandomSource.MWC_256.create()
    private var generatorSeed: Long

    init {
        generatorSeed = if (seed == 0) {
            RandomSource.createLong()
        } else {
            seed.toLong()
        }
    }

    /**
     * computeLCG() uses Apache's random number generator to compute l-bit integer m
     * in 2^(l-1) < m < 2^l along with a, b, seed values
    */
    fun computeLCG(): Long {

        // compute LCG modulus
        val m = calculateModulus()

        // compute random a, b, x0 in {0, ... m-1}
        // max is m-1, min is 0
        // rand.nextLong((m-1 - 0) + 1) + 0 = rand.nextLong(m)
        val a = rand.nextLong(m)
        val b = rand.nextLong(m)
        val x0 = rand.nextLong(m)

        return (a * x0 + b) % m
    }

    /**
     *  Use quadratic equation as LCG formula
     *  */
    fun computeQuadraticLCG(): Long {

        // compute LCG modulus
        val m = calculateModulus()

        // compute random a, b, x0 in {0, ... m-1}
        // max is m-1, min is 0
        // rand.nextLong((m-1 - 0) + 1) + 0 = rand.nextLong(m)
        val a = rand.nextLong(m)
        val b = rand.nextLong(m)
        val c = rand.nextLong(m)
        val x0 = rand.nextLong(m)

        return (a * x0 * x0 + b * x0 + c) % m
    }

    /**
     * Use cubic equation as LCG formula
     */
    fun computeCubicLCG(): Long {

        // compute LCG modulus
        val m = calculateModulus()

        // compute random a, b, x0 in {0, ... m-1}
        // max is m-1, min is 0
        // rand.nextLong((m-1 - 0) + 1) + 0 = rand.nextLong(m)
        val a = rand.nextLong(m)
        val b = rand.nextLong(m)
        val c = rand.nextLong(m)
        val d = rand.nextLong(m)
        val x0 = rand.nextLong(m)

        return (a * x0 * x0 * x0 + b * x0 * x0 + c * x0 + d) % m
    }

    private fun calculateModulus(): Long {
        val modulusBytes = DES.encrypt(seedToByteArray(), generateKey(), DESMode.CTR)
        return abs(ByteBuffer.wrap(modulusBytes).long)
    }

    private fun seedToByteArray(): ByteArray {
        return ByteBuffer.allocate(java.lang.Long.SIZE / java.lang.Byte.SIZE).putLong(generatorSeed++).array()
    }

    private fun generateKey(): ByteArray {
        val key = ByteArray(8)
        rand.nextBytes(key)
        return key
    }

}
