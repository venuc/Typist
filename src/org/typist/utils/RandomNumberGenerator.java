package org.typist.utils;

import java.util.Random;

public class RandomNumberGenerator {
    // Use this class to generate any random numbers.

    public static int getRandomParaNumber(int seed) {
        // Generate a number between 0 and seed (exclusive).
        Random rand = new Random();
        return rand.nextInt(seed);
    }

    public static void testMeth() {
        System.out.println("from testMeth: " + getRandomParaNumber(13));
    }
}
