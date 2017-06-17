package ru.opensecreto.sigmacoin.blockchain;

import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.config.BaseConfig;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockTest {

    @Test
    public void testEncoding() {
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

        byte[] hash1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash1);
        byte[] hash2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash2);
        byte[] hash3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash3);

        Block block1 = new Block(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), pow), new ArrayList<byte[]>() {{
            add(hash1);
            add(hash2);
            add(hash3);
        }});
        Block block2 = Block.decode(Block.encode(block1));
        assertThat(block2).isEqualTo(block1);
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

        byte[] hash1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash1);
        byte[] hash2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash2);
        byte[] hash3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash3);

        Block block1 = new Block(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), pow), new ArrayList<byte[]>() {{
            add(hash1);
            add(hash2);
            add(hash3);
        }});
        Block block2 = new Block(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), pow), new ArrayList<byte[]>() {{
            add(hash1);
            add(hash2);
            add(hash3);
        }});
        assertThat(block1.equals(block2)).isTrue();
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

        byte[] hash1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash1);
        byte[] hash2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash2);
        byte[] hash3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash3);

        Block block1 = new Block(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), pow), new ArrayList<byte[]>() {{
            add(hash1);
            add(hash2);
            add(hash3);
        }});
        assertThat(block1.equals(new Block(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(100), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), pow), new ArrayList<byte[]>() {{
            add(hash1);
            add(hash2);
            add(hash3);
        }}))).isFalse();
        assertThat(block1.equals(new Block(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), new int[4]), new ArrayList<byte[]>() {{
            add(hash1);
            add(hash2);
            add(hash3);
        }}))).isFalse();
        assertThat(block1.equals(new Block(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                a1, a2, a3
        ), pow), new ArrayList<byte[]>() {{
            add(hash1);
            add(hash2);
            add(new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH]);
        }}))).isFalse();
    }

    @Test
    public void testValuesUnmodifiable() {
        Random rnd = new Random();

        byte[] hash1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash1);
        byte[] hash2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash2);
        byte[] hash3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hash3);

        byte[] hashCopy1 = Arrays.copyOf(hash1, hash1.length);
        byte[] hashCopy2 = Arrays.copyOf(hash2, hash2.length);
        byte[] hashCopy3 = Arrays.copyOf(hash3, hash3.length);

        Block block1 = new Block(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH],
                new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH],
                new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH]
        ), new int[4]), new ArrayList<byte[]>() {{
            add(hashCopy1);
            add(hashCopy2);
            add(hashCopy3);
        }});
        hashCopy1[0] ^= 45;
        hashCopy1[10] ^= -45;
        hashCopy2[5] |= -100;
        hashCopy2[14] ^= 100;
        hashCopy2[2] *= -22;
        hashCopy2[20] += -30;
        assertThat(block1.transactionHashes.get(0)).containsExactly(hash1);
        assertThat(block1.transactionHashes.get(1)).containsExactly(hash2);
        assertThat(block1.transactionHashes.get(2)).containsExactly(hash3);
    }

}
