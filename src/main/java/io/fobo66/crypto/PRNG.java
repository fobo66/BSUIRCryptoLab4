/*

    LCG based on DES CTR mode

    Originally created by Iris Yuan as LCG implementation for learning purposes based on
    http://en.wikipedia.org/wiki/Linear_congruential_generator
    3/6/2014
*/
package io.fobo66.crypto;

import org.apache.commons.rng.UniformRandomProvider;
import org.apache.commons.rng.simple.RandomSource;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class PRNG {

    // used to generate parameters of congruential generators within modulus' range
    private final UniformRandomProvider rand = RandomSource.create(RandomSource.MWC_256);
    private long seed;

    public PRNG() {
        seed = RandomSource.createLong();
    }

    public PRNG(long seed) {
        this.seed = seed;
    }

    // computeLCG() uses Apache's random number generator to compute l-bit integer m
    // in 2^(l-1) < m < 2^l along with a, b, seed values

    // returns first n+1 elements x0, x1, xn in linear congruential sequence
    public long computeLCG() {

        // compute LCG modulus
        long m = calculateModulus();

        // compute random a, b, x0 in {0, ... m-1}
        // max is m-1, min is 0
        // rand.nextLong((m-1 - 0) + 1) + 0 = rand.nextLong(m)
        long a = rand.nextLong(m);
        long b = rand.nextLong(m);
        long x0 = rand.nextLong(m);

        return (a * x0 + b) % m;
    }

    private long calculateModulus() {
        byte[] modulusBytes = DES.encrypt(seedToByteArray(), generateKey(), DESMode.CTR);
        return Math.abs(ByteBuffer.wrap(modulusBytes).getLong());
    }

    @NotNull
    private byte[] seedToByteArray() {
        return ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(seed++).array();
    }

    @NotNull
    private byte[] generateKey() {
        byte[] key = new byte[8];
        rand.nextBytes(key);
        return key;
    }

}
