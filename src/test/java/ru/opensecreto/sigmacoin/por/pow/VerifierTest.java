package ru.opensecreto.sigmacoin.por.pow;

import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.digests.ShortenedDigest;
import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.core.DigestProvider;
import ru.opensecreto.sigmacoin.por.work.Miner;
import ru.opensecreto.sigmacoin.por.work.Verifier;

import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class VerifierTest {

    @Test
    public void testVerification() throws Exception {
        DigestProvider chunkProvider = () -> new ShortenedDigest(new SHA3Digest(256), 2);
        DigestProvider hashProvider = () -> new ShortenedDigest(new SHA3Digest(256), 4);
        final int N = 64;

        byte[] data = new byte[32];
        new Random().nextBytes(data);
        byte[] target = new byte[]{0x00, (byte) 0xf0, 0x00, 0x00};

        Miner miner = new Miner(data, chunkProvider, hashProvider, N, target, 100000);
        int[] solution = miner.call();

        byte[] dataCopy = Arrays.copyOf(data, data.length);
        byte[] targetCopy = Arrays.copyOf(target, target.length);
        int[] solutionCopy = Arrays.copyOf(solution, solution.length);

        Verifier verifier = new Verifier(chunkProvider, hashProvider, N);
        assertThat(verifier.verify(data, target, solution)).isTrue();

        //check original data is unchanged
        assertThat(data).containsExactly(dataCopy);
        assertThat(target).containsExactly(targetCopy);
        assertThat(solution).containsExactly(solutionCopy);
    }

    @Test
    public void testInvalidA() throws Exception {
        DigestProvider chunkProvider = () -> new ShortenedDigest(new SHA3Digest(256), 2);
        DigestProvider hashProvider = () -> new ShortenedDigest(new SHA3Digest(256), 4);
        final int N = 64;

        byte[] data = new byte[32];
        new Random().nextBytes(data);
        byte[] target = new byte[]{0x00, (byte) 0xf0, 0x00, 0x00};

        Miner miner = new Miner(data, chunkProvider, hashProvider, N, target, 100000);
        int[] solution = miner.call();

        Verifier verifier = new Verifier(chunkProvider, hashProvider, N);
        data[3] ^= 35;

        assertThat(verifier.verify(data, target, solution)).isFalse();
    }

    @Test
    public void testInvalidB() throws Exception {
        DigestProvider chunkProvider = () -> new ShortenedDigest(new SHA3Digest(256), 2);
        DigestProvider hashProvider = () -> new ShortenedDigest(new SHA3Digest(256), 4);
        final int N = 64;

        byte[] data = new byte[32];
        new Random().nextBytes(data);
        byte[] target = new byte[]{0x00, (byte) 0xf0, 0x00, 0x00};

        Miner miner = new Miner(data, chunkProvider, hashProvider, N, target, 100000);
        int[] solution = miner.call();

        Verifier verifier = new Verifier(chunkProvider, hashProvider, N);
        solution[2] ^= 5668;
        solution[4] ^= 256;

        assertThat(verifier.verify(data, target, solution)).isFalse();
    }

    @Test
    public void testInvalidSolutionSize() throws Exception {
        DigestProvider chunkProvider = () -> new ShortenedDigest(new SHA3Digest(256), 2);
        DigestProvider hashProvider = () -> new ShortenedDigest(new SHA3Digest(256), 4);
        final int N = 64;

        byte[] data = new byte[32];
        new Random().nextBytes(data);
        byte[] target = new byte[]{0x00, (byte) 0xf0, 0x00, 0x00};

        Miner miner = new Miner(data, chunkProvider, hashProvider, N, target, 100000);
        int[] solution = miner.call();

        Verifier verifier = new Verifier(chunkProvider, hashProvider, N - 1);

        assertThat(verifier.verify(data, target, solution)).isFalse();
    }

}
