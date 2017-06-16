package ru.opensecreto.sigmacoin.blockchain;

import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.config.BaseConfig;

import java.math.BigInteger;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class RawBlockHeaderTest {

    @Test
    public void testSerialization() {
        byte[] a1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];

        Random rnd = new Random();
        rnd.nextBytes(a1);
        rnd.nextBytes(a2);
        rnd.nextBytes(a3);

        RawBlockHeader header1 = new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        );
        RawBlockHeader header2 = RawBlockHeader.decode(RawBlockHeader.encode(header1));
        assertThat(header2).isEqualTo(header1);
    }

    @Test
    public void testEqualsTrue() {
        byte[] a1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];

        Random rnd = new Random();
        rnd.nextBytes(a1);
        rnd.nextBytes(a2);
        rnd.nextBytes(a3);

        RawBlockHeader header1 = new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        );
        RawBlockHeader header2 = new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        );
        assertThat(header1.equals(header2)).isTrue();
    }

    @Test
    public void testEqualsFalse() {
        byte[] a1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];

        Random rnd = new Random();
        rnd.nextBytes(a1);
        rnd.nextBytes(a2);
        rnd.nextBytes(a3);

        RawBlockHeader header = new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        );

        assertThat(header.equals(null)).isFalse();
        assertThat(header.equals("")).isFalse();
        assertThat(header.equals(new RawBlockHeader(
                BigInteger.valueOf(100), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ))).isFalse();
        assertThat(header.equals(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(200), BigInteger.valueOf(3),
                a1, a2, a3
        ))).isFalse();
        assertThat(header.equals(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(300),
                a1, a2, a3
        ))).isFalse();
        assertThat(header.equals(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                new byte[32], a2, a3
        ))).isFalse();
        assertThat(header.equals(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, new byte[32], a3
        ))).isFalse();
        assertThat(header.equals(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, new byte[32]
        ))).isFalse();
        assertThat(header.equals(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ))).isFalse();
    }

}
