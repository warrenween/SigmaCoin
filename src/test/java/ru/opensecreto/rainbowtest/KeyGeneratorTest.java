package ru.opensecreto.rainbowtest;

import org.testng.annotations.Test;
import ru.opensecreto.rainbow.KeyGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class KeyGeneratorTest {

    @Test
    public void testIllegalArguments() {
        assertThatThrownBy(() -> new KeyGenerator(256, new int[]{-1, 1, 10}));
        assertThatThrownBy(() -> new KeyGenerator(256, new int[]{0, 1, 10}));
        assertThatThrownBy(() -> new KeyGenerator(256, new int[]{1, 1, 10}));
        new KeyGenerator(256, new int[]{1, 2, 3});
    }

    @Test
    public void testParams() {
        KeyGenerator gen = new KeyGenerator(256, new int[]{6, 12, 17, 22, 33});

        assertThat(gen.getn()).isEqualTo(33);
        assertThat(gen.getS()).containsExactly(new int[][]{
                {1, 2, 3, 4, 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33}
        });
        assertThat(gen.getv()).containsExactly(6, 12, 17, 22, 33);
        assertThat(gen.geto()).containsExactly(6, 5, 5, 11);
        assertThat(gen.getu()).isEqualTo(5);
    }

}
