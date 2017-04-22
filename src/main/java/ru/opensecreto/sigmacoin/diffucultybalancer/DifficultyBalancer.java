package ru.opensecreto.sigmacoin.diffucultybalancer;

import org.nd4j.linalg.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class DifficultyBalancer {

    private final BigInteger target_time;
    /**
     * 0 <= smoothRate < 1
     */
    private final BigDecimal smoothRate;

    public DifficultyBalancer(BigInteger target_time, BigDecimal smoothRate) {
        if (target_time.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("target_time must be larger than 0");
        }
        this.target_time = target_time;

        if (smoothRate.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("smoothRate must be grater or equal to 0");
        if (smoothRate.compareTo(BigDecimal.ONE) >= 0)
            throw new IllegalArgumentException("smoothRate must be less than 1");
        this.smoothRate = smoothRate;
    }

    public final BigInteger getNewDifficulty(BigInteger lastDiffuculty, BigInteger lastTime) {
        BigDecimal k;
        if (lastTime.compareTo(target_time) == 0) {
            return lastDiffuculty;
        }

        k = BigDecimalMath.pow(new BigDecimal(lastTime).divide(new BigDecimal(target_time)),
                BigDecimal.ONE.subtract(smoothRate));

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
