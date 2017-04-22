package ru.opensecreto.sigmacoin.diffucultybalancer;

import org.nd4j.linalg.util.BigDecimalMath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class DifficultyBalancer {

    private final BigInteger targetTime;
    /**
     * 0 <= smoothRate < 1
     */
    private final BigDecimal smoothRate;

    private final MathContext contextNoRound;
    private final MathContext contextCeil;
    private final MathContext contextFloor;


    public DifficultyBalancer(BigInteger targetTime, BigDecimal smoothRate, int precision) {
        if (targetTime.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("targetTime must be larger than 0");
        }
        this.targetTime = targetTime;

        if (smoothRate.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("smoothRate must be greater or equal to 0");
        if (smoothRate.compareTo(BigDecimal.ONE) >= 0)
            throw new IllegalArgumentException("smoothRate must be less than 1");
        this.smoothRate = smoothRate;

        contextNoRound = new MathContext(precision, RoundingMode.UNNECESSARY);
        contextCeil = new MathContext(precision, RoundingMode.CEILING);
        contextFloor = new MathContext(precision, RoundingMode.FLOOR);
    }

    public final BigInteger getNewDifficulty(BigInteger lastDiffuculty, BigInteger lastTime) {
        if (lastTime.compareTo(targetTime) == 0) {
            return lastDiffuculty;
        }

        //k = (lastTime/targetTime)^(1-smoothRate)
        BigDecimal k = BigDecimalMath.pow(new BigDecimal(lastTime).divide(new BigDecimal(targetTime), contextNoRound),
                BigDecimal.ONE.subtract(smoothRate));

        //last time is bigger than target
        if (lastTime.compareTo(targetTime) > 0) {
            return new BigDecimal(lastDiffuculty).multiply(k).round(contextFloor).toBigInteger();
        }

        //last time is lower than target
        if (lastTime.compareTo(targetTime) < 0) {
            return new BigDecimal(lastDiffuculty).multiply(k).round(contextCeil).toBigInteger();
        }

        throw new IllegalStateException("unexpected error");
    }
}
