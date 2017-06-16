package ru.opensecreto.sigmacoin.blockchain;

import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.config.BaseConfig;

import java.math.BigInteger;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class FullBlockHeaderTest {

    @Test
    public void testSerialization() {
        byte[] a1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        int[] pow = new int[4];

        Random rnd = new Random();
        rnd.nextBytes(a1);
        rnd.nextBytes(a2);
        rnd.nextBytes(a3);
        for (int i = 0; i < pow.length; i++) {
            pow[i] = rnd.nextInt();
        }

        FullBlockHeader header1 = new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), pow);
        FullBlockHeader header2 = FullBlockHeader.decode(FullBlockHeader.encode(header1));
        assertThat(header2).isEqualTo(header1);
    }

    @Test
    public void testEqualsTrue() {
        byte[] a1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        int[] pow = new int[4];

        Random rnd = new Random();
        rnd.nextBytes(a1);
        rnd.nextBytes(a2);
        rnd.nextBytes(a3);
        for (int i = 0; i < pow.length; i++) {
            pow[i] = rnd.nextInt();
        }

        FullBlockHeader header1 = new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), pow);
        FullBlockHeader header2 = new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), pow);
        assertThat(header1.equals(header2)).isTrue();
    }

    @Test
    public void testEqualsFalse() {
        byte[] a1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        byte[] a3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        int[] pow = new int[4];

        Random rnd = new Random();
        rnd.nextBytes(a1);
        rnd.nextBytes(a2);
        rnd.nextBytes(a3);
        for (int i = 0; i < pow.length; i++) {
            pow[i] = rnd.nextInt();
        }

        FullBlockHeader header = new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), pow);

        assertThat(header.equals(null)).isFalse();
        assertThat(header.equals("")).isFalse();
        assertThat(header.equals(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(10), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3), pow)
        )).isFalse();
        assertThat(header.equals(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3), new int[4])
        )).isFalse();
    }

}
