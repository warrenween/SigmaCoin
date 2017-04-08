package ru.opensecreto.sigmacoin.diffucultybalancer;

import java.math.BigInteger;

public class DifficultyBalancer {

    private final int target_time;
    /**
     * 0 < smooth_k < 1
     */
    private final double smooth_k;

    public DifficultyBalancer(int target_time, double smooth_k) {
        this.target_time = target_time;

        if (smooth_k <= 0) throw new IllegalArgumentException("smooth_k must be grater than 0");
        if (smooth_k >= 1) throw new IllegalArgumentException("smooth_k must be less than 1");
        this.smooth_k = smooth_k;
    }

    public final int getNewDifficulty(int lastDiffuculty, int lastTime) {
        return (int) (lastDiffuculty * Math.pow(lastTime / target_time, 1 - smooth_k));
    }
}
