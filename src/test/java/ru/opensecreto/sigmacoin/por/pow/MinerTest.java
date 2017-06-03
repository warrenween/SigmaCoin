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
        DigestProvider provider = () -> new ShortenedDigest(new SHA3Digest(256), 1);

        byte[] data = new byte[16];
        new Random().nextBytes(data);
        byte[] dataCopy = Arrays.copyOf(data, 16);

        byte[] target = new byte[]{0x08};
        byte[] targetCopy = new byte[]{0x08};

        Miner miner = new Miner(data, provider, 8, target, 10000);
        int[] result = miner.call();

        //assert original arrays are unchanged
        assertThat(data).containsExactly(dataCopy);
        assertThat(target).containsExactly(targetCopy);

        //checking valid data (not hash)
        assertThat(result).hasSize(8);
        //checking ordered. Ids are unsigned
        long[] tmp = new long[8];
        for (int i = 0; i < result.length; i++) {
            tmp[i] = result[i] & 0xffffffffL;
        }
        long previous = tmp[0];
        for (int i = 1; i < tmp.length; i++) {
            assertThat(tmp[i]).overridingErrorMessage("Array is expected to be ordered.").isGreaterThan(previous);
        }

        Digest digest = provider.getDigest();
        digest.update(data, 0, data.length);
        for (int i = 0; i < result.length; i++) {
            digest.update(Ints.toByteArray(result[i]), 0, Integer.BYTES);
        }
        byte[] hash = new byte[1];
        digest.doFinal(hash, 0);
        assertThat((hash[0] & 0xff) < 0x0f);

    }

}
