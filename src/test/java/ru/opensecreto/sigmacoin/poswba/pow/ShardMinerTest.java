package ru.opensecreto.sigmacoin.poswba.pow;

import com.google.common.primitives.Longs;
import org.assertj.core.api.Assertions;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.crypto.Ed25519.Ed25519KeyGenerator;
import ru.opensecreto.sigmacoin.crypto.Ed25519.Ed25519PublicKey;
import ru.opensecreto.sigmacoin.poswba.storage.Shard;
import ru.opensecreto.sigmacoin.poswba.work.ShardMiner;

public class ShardMinerTest {

    @Test
    public void testShardMining() throws Exception {
        Ed25519PublicKey publicKey = (Ed25519PublicKey) new Ed25519KeyGenerator().generateKeypair().getPublicKey();

        ShardMiner miner = new ShardMiner(publicKey, 134217728, () -> new KeccakDigest(16), 3);

        Shard shard = miner.call();

        Assertions.assertThat(shard.publicKey).isEqualTo(publicKey);

        //checking if solution valid
        byte[] result = new byte[new KeccakDigest(16).getDigestSize()];

        KeccakDigest digest = new KeccakDigest(16);
        digest.update(Longs.toByteArray(shard.getIds()[0]), 0, 8);
        digest.update(shard.getSeed(), 0, 64);
        digest.update(publicKey.getPublicKey(), 0, publicKey.getPublicKey().length);
        digest.doFinal(result, 0);

        digest.reset();
        digest.update(Longs.toByteArray(shard.getIds()[1]), 0, 8);
        digest.update(shard.getSeed(), 0, 64);
        digest.update(publicKey.getPublicKey(), 0, publicKey.getPublicKey().length);
        byte[] tmp = new byte[new KeccakDigest(16).getDigestSize()];
        digest.doFinal(tmp, 0);

        //xor
        for (int i = 0; i < result.length; i++) {
            result[i] ^= tmp[i];
        }

        Assertions.assertThat(result).containsOnly((byte) 0);
    }

}
