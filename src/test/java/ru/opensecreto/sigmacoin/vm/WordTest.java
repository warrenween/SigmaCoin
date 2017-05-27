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
        assertThat(new Word(4).equals(new Word(-4))).isFalse();
        assertThat(new Word(0x01).equals(new Word(0x10))).isFalse();

        assertThat(new Word(4).equals(new Word(4))).isTrue();
    }

    @Test
    public void testSum() {
        Word a1 = new Word(parseHexBinary("d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35"));
        Word b1 = new Word(parseHexBinary("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"));
        Word result1 = a1.sum(b1);
        assertThat(result1.equals(new Word(parseHexBinary("b824227cbf5a33037b3b4e3a250b1627294a49bd1b60b2dd7ecfff8a6466638a"))));

        Word a2 = new Word(123456);
        Word b2 = new Word(-123456);
        Word result2 = a2.sum(b2);
        assertThat(result2.equals(new Word(0)));
    }

    @Test
    public void testSumOpposite() {

    }

    @Test
    public void testNegative() {
        assertThat(new Word(-1).isNegative()).isTrue();
    }

    @Test
    public void testPositive() {
        assertThat(new Word(1).isPositive()).isTrue();
    }

    @Test
    public void testCompare() {
        assertThat(new Word(1).compareTo(new Word(-1)) > 0).isTrue();
        assertThat(new Word(-1).compareTo(new Word(1)) < 0).isTrue();
        assertThat(new Word(1).compareTo(new Word(1)) == 0).isTrue();
        assertThat(new Word(-1).compareTo(new Word(-1)) == 0).isTrue();

        assertThat(new Word(-10).compareTo(new Word(-10)) == 0).isTrue();
        assertThat(new Word(-10).compareTo(new Word(10)) < 0).isTrue();
        assertThat(new Word(10).compareTo(new Word(-10)) > 0).isTrue();
    }

    @Test
    public void testMultiply() {
        assertThat(new Word(0).multiply(new Word(0))).isEqualTo(new Word(0));
        assertThat(new Word(-1).multiply(new Word(0))).isEqualTo(new Word(0));
        assertThat(new Word(0).multiply(new Word(-1))).isEqualTo(new Word(0));

        assertThat(new Word(12).multiply(new Word(12))).isEqualTo(new Word(144));
        assertThat(new Word(12).multiply(new Word(-12))).isEqualTo(new Word(-144));
        assertThat(new Word(-12).multiply(new Word(12))).isEqualTo(new Word(-144));
        assertThat(new Word(-12).multiply(new Word(-12))).isEqualTo(new Word(144));

        assertThat(new Word(512).multiply(new Word(986))).isEqualTo(new Word(504832));
        assertThat(new Word(512).multiply(new Word(-986))).isEqualTo(new Word(-504832));
        assertThat(new Word(-512).multiply(new Word(986))).isEqualTo(new Word(-504832));
        assertThat(new Word(-512).multiply(new Word(-986))).isEqualTo(new Word(504832));
    }

    @Test
    public void testInRange() {
        assertThat(new Word(10).isInRange(new Word(0), new Word(100))).isTrue();
        assertThat(new Word(0).isInRange(new Word(-20), new Word(100))).isTrue();
        assertThat(new Word(10).isInRange(new Word(0), new Word(100))).isTrue();
        assertThat(new Word(10).isInRange(new Word(20), new Word(100))).isFalse();
        assertThat(new Word(-10).isInRange(new Word(0), new Word(100))).isFalse();
        assertThat(new Word(-10).isInRange(new Word(-10), new Word(100))).isTrue();
        assertThat(new Word(-20).isInRange(new Word(-100), new Word(-10))).isTrue();
    }
}
