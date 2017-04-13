package ru.opensecreto.sigmacoin.diffucultybalancer;

import org.nd4j.linalg.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class DifficultyBalancer {

    private final BigInteger target_time;
    /**
     * 0 <= smooth_k < 1
     */
    private final BigDecimal smooth_k;

    public DifficultyBalancer(BigInteger target_time, BigDecimal smooth_k) {
        if (target_time.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("target_time must be larger than 0");
        }
        this.target_time = target_time;

        if (smooth_k.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("smooth_k must be grater or equal to 0");
        if (smooth_k.compareTo(BigDecimal.ONE) >= 0)
            throw new IllegalArgumentException("smooth_k must be less than 1");
        this.smooth_k = smooth_k;
    }

    public final BigInteger getNewDifficulty(BigInteger lastDiffuculty, BigInteger lastTime) {
        BigDecimal k;
        if (lastTime.compareTo(target_time) == 0) {
            return lastDiffuculty;
        }

        k = BigDecimalMath.pow(new BigDecimal(lastTime).divide(new BigDecimal(target_time)),
                BigDecimal.ONE.subtract(smooth_k));

        //last time is bigger than target
        if (lastTime.compareTo(target_time) > 0) {
            return new BigDecimal(lastDiffuculty).multiply(k).round(new MathContext(2, RoundingMode.FLOOR)).toBigInteger();
        }

        //last time is lower than target
        if (lastTime.compareTo(target_time) < 0) {
            return new BigDecimal(lastDiffuculty).multiply(k).round(new MathContext(2, RoundingMode.CEILING)).toBigInteger();
        }

        throw new IllegalStateException("unexcpected error");
    }
}
