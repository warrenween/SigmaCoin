package ru.opensecreto;

import java.util.Random;

public class TestUtils {

    private static final Random RANDOM = new Random();

    public static byte[] getFixedArray(int size) {
        byte[] data = new byte[size];
        RANDOM.nextBytes(data);
        return data;
    }

    public static byte[] getRandomArray(int bound) {
        return getFixedArray(RANDOM.nextInt(bound));
    }

}
