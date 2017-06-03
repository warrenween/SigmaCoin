package ru.opensecreto.sigmacoin.por.pow;

import com.google.common.primitives.Ints;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.digests.ShortenedDigest;
import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.core.DigestProvider;
import ru.opensecreto.sigmacoin.por.work.Miner;

import java.util.Arrays;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class MinerTest {

    @Test
    public void test() throws Exception {
        DigestProvider chunkProvider = () -> new ShortenedDigest(new SHA3Digest(256), 3);
        DigestProvider hashProvider = () -> new ShortenedDigest(new SHA3Digest(256), 8);

        byte[] data = new byte[16];
        new Random().nextBytes(data);
        byte[] dataCopy = Arrays.copyOf(data, 16);

        byte[] target = new byte[]{0x00, 0x00, 0x00, (byte) 0xff, 0x00, 0x00, 0x00, 0x00};
        byte[] targetCopy = new byte[]{0x00, 0x00, 0x00, (byte) 0xff, 0x00, 0x00, 0x00, 0x00};

        Miner miner = new Miner(data, chunkProvider, hashProvider, 16, target, 100000);
        int[] result = miner.call();

        //assert original arrays are unchanged
        assertThat(data).containsExactly(dataCopy);
        assertThat(target).containsExactly(targetCopy);

        //checking valid data (not hash)
        assertThat(result).hasSize(16);
        //checking ordered. Ids are unsigned
        long[] tmp = new long[16];
        for (int i = 0; i < result.length; i++) {
            tmp[i] = result[i] & 0xffffffffL;
        }
        long previous = tmp[0];
        for (int i = 1; i < tmp.length; i++) {
            assertThat(tmp[i]).overridingErrorMessage("Array is expected to be ordered.").isGreaterThan(previous);
        }

        //checking xor is 0
        byte[] chunkResult = new byte[0];
        Digest chunkDigest = chunkProvider.getDigest();
        //first value
        chunkDigest.update(Ints.toByteArray(result[0]), 0, Integer.BYTES);
        chunkDigest.update(data, 0, data.length);
        chunkDigest.doFinal(chunkResult, 0);

        for (int i = 1; i < result.length; i++) {
            byte[] chunkTmp = new byte[3];
            chunkDigest.reset();
            chunkDigest.update(Ints.toByteArray(result[0]), 0, Integer.BYTES);
            chunkDigest.update(data, 0, data.length);
            chunkDigest.doFinal(chunkTmp, 0);
            for (int j = 0; j < chunkTmp.length; j++) {
                chunkResult[j] ^= chunkTmp[j];
            }
        }

        for (byte aChunkResult : chunkResult) {
            assertThat(aChunkResult).isEqualTo((byte) 0);
        }


        //checking final
        Digest hashDigest = hashProvider.getDigest();
        hashDigest.update(data, 0, data.length);
        for (int i = 0; i < result.length; i++) {
            hashDigest.update(Ints.toByteArray(result[i]), 0, Integer.BYTES);
        }
        byte[] hash = new byte[2];
        hashDigest.doFinal(hash, 0);
        assertThat(hash[0] == 0).isTrue();
        assertThat((hash[1] & 0xff) < 0xff).isTrue();

    }

}
