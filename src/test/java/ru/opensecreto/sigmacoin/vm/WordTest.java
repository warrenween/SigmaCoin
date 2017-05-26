package ru.opensecreto.sigmacoin.vm;

import org.testng.annotations.Test;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static org.assertj.core.api.Assertions.assertThat;

public class WordTest {

    @Test
    public void testEquals() {
        assertThat(new Word(4).equals(null)).isFalse();
        assertThat(new Word(4).equals("a")).isFalse();
        assertThat(new Word(4).equals(new Word(3))).isFalse();
        assertThat(new Word(4).equals(new Word(4))).isTrue();
    }

    @Test
    public void testSum() {
        Word a = new Word(parseHexBinary("d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35"));
        Word b = new Word(parseHexBinary("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"));
        Word result = a.sum(b);
        assertThat(result.equals(new Word(parseHexBinary("b824227cbf5a33037b3b4e3a250b1627294a49bd1b60b2dd7ecfff8a6466638a"))));
    }

    @Test
    public void testNegative() {
        assertThat(new Word(-1).isNegative()).isTrue();
    }

}
