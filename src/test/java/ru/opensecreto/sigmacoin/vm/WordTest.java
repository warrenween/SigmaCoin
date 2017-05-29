package ru.opensecreto.sigmacoin.vm;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(new Word(0).sum(new Word(0)).equals(new Word(0)));
        assertThat(new Word(123456).sum(new Word(-123456)).equals(new Word(0)));
        assertThat(new Word(-123456).sum(new Word(123456)).equals(new Word(0)));
        assertThat(new Word(20).sum(new Word(30)).equals(new Word(50)));
        assertThat(new Word(-20).sum(new Word(30)).equals(new Word(10)));
        assertThat(new Word(20).sum(new Word(-30)).equals(new Word(-10)));
        assertThat(new Word(12565895).sum(new Word(154894949)).equals(new Word(167460844)));
        assertThat(new Word(12565895).sum(new Word(-154894949)).equals(new Word(-142329054)));
        assertThat(new Word(-12565895).sum(new Word(154894949)).equals(new Word(142329054)));
        assertThat(new Word(-12565895).sum(new Word(-154894949)).equals(new Word(142329054)));
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

    @Test
    public void testSubtract() {
        assertThat(new Word(100).subtract(new Word(10))).isEqualTo(new Word(90));
    }

    @Test
    public void testNegate() {
        Word a = new Word(100);
        Word b = a.negate();
        assertThat(a).isEqualTo(new Word(100));
        assertThat(b).isEqualTo(new Word(-100));
    }

    @Test
    public void testDivision() {
        assertThat(new Word(7).div(new Word(2))).isEqualTo(new Word(3));
        assertThat(new Word(7).mod(new Word(2))).isEqualTo(new Word(1));

        assertThat(new Word(-7).div(new Word(2))).isEqualTo(new Word(-3));
        assertThat(new Word(-7).mod(new Word(2))).isEqualTo(new Word(-1));

        assertThat(new Word(7).div(new Word(-2))).isEqualTo(new Word(-3));
        assertThat(new Word(7).mod(new Word(-2))).isEqualTo(new Word(1));

        assertThat(new Word(-7).div(new Word(-2))).isEqualTo(new Word(3));
        assertThat(new Word(-7).mod(new Word(-2))).isEqualTo(new Word(-1));

        assertThat(new Word(8).div(new Word(2))).isEqualTo(new Word(4));
        assertThat(new Word(8).mod(new Word(2))).isEqualTo(new Word(0));

        assertThatThrownBy(() -> new Word(10).div(Word.WORD_0)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> new Word(10).mod(Word.WORD_0)).isInstanceOf(ArithmeticException.class);
    }
}
