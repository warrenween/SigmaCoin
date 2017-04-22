package ru.opensecreto.sigmacoin.difficultybalancer;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.diffucultybalancer.DifficultyBalancer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DifficultyBalancerTest {

    @Test
    public void testDifficultyChangesInCorrectDirection() {
        DifficultyBalancer balancer = new DifficultyBalancer(
                BigInteger.valueOf(100),
                BigDecimal.ZERO,
                5
        );

        assertThat(balancer.getNewDifficulty(
                BigInteger.valueOf(100), BigInteger.valueOf(100)
        )).isEqualByComparingTo(BigInteger.valueOf(100));
        assertThat(balancer.getNewDifficulty(
                BigInteger.valueOf(100), BigInteger.valueOf(50)
        )).isEqualByComparingTo(BigInteger.valueOf(50));
        assertThat(balancer.getNewDifficulty(
                BigInteger.valueOf(100), BigInteger.valueOf(200)
        )).isEqualByComparingTo(BigInteger.valueOf(200));
    }

    @Test
    public void testArguments() {
        new DifficultyBalancer(BigInteger.TEN, BigDecimal.ZERO, 5);

        assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.ZERO, new BigDecimal("0.5"), 5));
        assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.TEN, BigDecimal.ONE, 5));

        assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.TEN.negate(), new BigDecimal("0.5"), 5));
        assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.TEN, new BigDecimal("-0.5"), 5));
        assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.TEN, new BigDecimal("1.5"), 5));

        assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.TEN, BigDecimal.ZERO, 5).getNewDifficulty(
                BigInteger.ONE, new BigInteger("-10")
        ));
    }

    @Test
    public void test() {
        //test fractions
        //this wil do 2/7 which can lead to exception
        new DifficultyBalancer(BigInteger.valueOf(7), BigDecimal.ZERO, 5)
                .getNewDifficulty(BigInteger.ONE, BigInteger.valueOf(2));

        new DifficultyBalancer(BigInteger.valueOf(7), BigDecimal.valueOf(0.137), 5)
                .getNewDifficulty(BigInteger.ONE, BigInteger.valueOf(2));

    }

}
