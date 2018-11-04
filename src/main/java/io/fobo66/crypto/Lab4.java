package io.fobo66.crypto;

public class Lab4 {
    public static void main(String[] args) {
        PRNG prng = new PRNG();
        for (int i = 0; i < 100; i++) {
            System.out.println(prng.computeLCG());
        }
    }
}