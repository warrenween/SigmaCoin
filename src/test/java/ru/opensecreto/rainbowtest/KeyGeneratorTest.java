package ru.opensecreto.rainbowtest;

import org.testng.annotations.Test;
import ru.opensecreto.rainbow.KeyGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class KeyGeneratorTest {

    @Test
    public void testIllegalArguments() {
        assertThatThrownBy(() -> new KeyGenerator(new int[]{-1, 1, 10}));
        assertThatThrownBy(() -> new KeyGenerator(new int[]{0, 1, 10}));
        assertThatThrownBy(() -> new KeyGenerator(new int[]{1, 1, 10}));
        new KeyGenerator(new int[]{1, 2, 3});
    }

    @Test
    public void testParams() {
        KeyGenerator gen = new KeyGenerator(new int[]{6, 12, 17, 22, 33});

        assertThat(gen.getN()).isEqualTo(33);
        assertThat(gen.getS()).containsExactly(
                1, 2, 3, 4, 5, 6, 7, 8, 9,
                10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                20, 21, 22, 23, 24, 25, 26, 27, 28, 29,
                30, 31, 32, 33);
        assertThat(gen.getVi()).containsExactly(6, 12, 17, 22, 33);
        assertThat(gen.getOi()).containsExactly(6, 5, 5, 11);
        assertThat(gen.getU()).isEqualTo(5);
    }

}
