package ru.opensecreto.sigmacoin.vm;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class VMConfigurationTest {

    @Test
    public void test() {
        VMConfiguration configuration = new VMConfiguration(28, 33, 54);
        Assertions.assertThat(configuration.frameMaxStackSize).isEqualTo(28);
        Assertions.assertThat(configuration.contractIdLength).isEqualTo(33);
        Assertions.assertThat(configuration.maxCallDepth).isEqualTo(54);
    }

    @Test
    public void testNearlyBadValues() {
        VMConfiguration configuration = new VMConfiguration(1, 1, 1);
        Assertions.assertThat(configuration.frameMaxStackSize).isEqualTo(1);
        Assertions.assertThat(configuration.contractIdLength).isEqualTo(1);
        Assertions.assertThat(configuration.maxCallDepth).isEqualTo(1);
    }

    @Test
    public void testBadValues() {
        //test zeroes
        Assertions.assertThatThrownBy(() -> new VMConfiguration(0, 1, 1))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> new VMConfiguration(1, 0, 1))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> new VMConfiguration(1, 1, 0))
                .isInstanceOf(IllegalArgumentException.class);

        //test negatives
        Assertions.assertThatThrownBy(() -> new VMConfiguration(-10, 10, 10))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> new VMConfiguration(10, -10, 10))
                .isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> new VMConfiguration(10, 10, -10))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
