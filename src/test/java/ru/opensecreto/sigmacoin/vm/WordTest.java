package ru.opensecreto.sigmacoin.vm;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WordTest {

    @Test
    public void testEquals() {
        assertThat(new Word(4).equals(null)).isFalse();
        assertThat(new Word(4).equals("a")).isFalse();
        assertThat(new Word(4).equals(new Word(3))).isFalse();
        assertThat(new Word(4).equals(new Word(4))).isTrue();
    }

}
