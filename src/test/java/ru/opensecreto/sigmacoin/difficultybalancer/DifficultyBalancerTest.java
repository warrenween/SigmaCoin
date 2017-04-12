package ru.opensecreto.sigmacoin.difficultybalancer;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.diffucultybalancer.DifficultyBalancer;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DifficultyBalancerTest {

    @Test
    public void testDifficultyChangesInCorrectDirection() {
        DifficultyBalancer balancer = new DifficultyBalancer(
                BigInteger.valueOf(100),
                BigDecimal.ZERO
        );

        Assertions.assertThat(balancer.getNewDifficulty(
                BigInteger.valueOf(100), BigInteger.valueOf(100)
        )).isEqualByComparingTo(BigInteger.valueOf(100));
        Assertions.assertThat(balancer.getNewDifficulty(
                BigInteger.valueOf(100), BigInteger.valueOf(50)
        )).isEqualByComparingTo(BigInteger.valueOf(50));
        Assertions.assertThat(balancer.getNewDifficulty(
                BigInteger.valueOf(100), BigInteger.valueOf(200)
        )).isEqualByComparingTo(BigInteger.valueOf(200));
    }

    @Test
    public void testArguments() {
        new DifficultyBalancer(BigInteger.TEN, BigDecimal.ZERO);

        Assertions.assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.ZERO, new BigDecimal("0.5")));
        Assertions.assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.TEN, BigDecimal.ONE));

        Assertions.assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.TEN.negate(), new BigDecimal("0.5")));
        Assertions.assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.TEN, new BigDecimal("-0.5")));
        Assertions.assertThatThrownBy(() -> new DifficultyBalancer(BigInteger.TEN, new BigDecimal("1.5")));
    }

}
