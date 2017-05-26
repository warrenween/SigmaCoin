package ru.opensecreto.sigmacoin.vm;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class VMConfigurationTest {

    @Test
    public void test() {
        VMConfiguration configuration = new VMConfiguration(28, 54, 94);
        assertThat(configuration.stackSize).isEqualTo(28);
        assertThat(configuration.maxCallDepth).isEqualTo(54);
        assertThat(configuration.memorySize).isEqualTo(94);
    }

    @Test
    public void testNearlyBadValues() {
        VMConfiguration configuration = new VMConfiguration(1, 1, 1);
        assertThat(configuration.stackSize).isEqualTo(1);
        assertThat(configuration.maxCallDepth).isEqualTo(1);
        assertThat(configuration.memorySize).isEqualTo(1);
    }

    @Test
    public void testBadValues() {
        //test zeroes
        assertThatThrownBy(() -> new VMConfiguration(0, 1, 1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new VMConfiguration(1, 0, 1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new VMConfiguration(1, 1, 0))
                .isInstanceOf(IllegalArgumentException.class);

        //test negatives
        assertThatThrownBy(() -> new VMConfiguration(-10, 10, 10))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new VMConfiguration(10, -10, 10))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new VMConfiguration(10, 10, -10))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
