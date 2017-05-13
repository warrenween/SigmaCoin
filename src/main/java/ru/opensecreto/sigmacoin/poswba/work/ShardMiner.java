package ru.opensecreto.sigmacoin.poswba.work;

import com.google.common.primitives.Longs;
import org.bouncycastle.crypto.Digest;
import ru.opensecreto.sigmacoin.core.DigestProvider;
import ru.opensecreto.sigmacoin.crypto.Ed25519.Ed25519PublicKey;
import ru.opensecreto.sigmacoin.poswba.storage.Shard;

import java.util.Random;
import java.util.concurrent.Callable;

public class ShardMiner implements Callable<Shard> {

    private final byte[] seed = new byte[64];
    private final Ed25519PublicKey publicKey;

    private final int digestLength;
    private final int maxChunkCount;
    private final DigestProvider digestProvider;
    private final int n;

    private long chunksGenerated;

    /**
     * Set up ShardMiner.
     *
     * @param publicKey      public key. Shard will be tied to public key.
     * @param maxChunkCount  max count of chunks stored in memory. Chunk length is equal to output length of digest
     * @param digestProvider hash function for generating shards
     */
    public ShardMiner(Ed25519PublicKey publicKey, int maxChunkCount, DigestProvider digestProvider, int n) {
        //checking
        if (publicKey == null) throw new IllegalArgumentException("PublicKey can not be null");
        if (maxChunkCount <= 0) throw new IllegalArgumentException("maxChunkCount must be>0");
        if (digestProvider == null) throw new IllegalArgumentException("digestProvide can not be null");
        if (n <= 1) throw new IllegalArgumentException("n must be >1");

        if (maxChunkCount < n) throw new IllegalArgumentException("maxChunk can not be less than n");
        if (digestProvider.getDigest() == null)
            throw new IllegalArgumentException("digestProvider can not return null");

        //setting
        this.publicKey = publicKey;
        digestLength = digestProvider.getDigest().getDigestSize();
        this.maxChunkCount = maxChunkCount;
        this.digestProvider = digestProvider;
        this.n = n;

        new Random().nextBytes(seed);
    }

    @Override
    public Shard call() throws Exception {
        byte[][] chunks = new byte[maxChunkCount][digestLength];
        Digest digest = digestProvider.getDigest();

        int[] ids = new int[n];
        byte[] result = new byte[digestLength];

        //check id of last of next chunks is less than 8 bit
        while (chunksGenerated < (Long.MAX_VALUE - maxChunkCount)) {
            for (int i = 0; i < maxChunkCount; i++) {
                ids[i] = i;
            }

            //generating chunk
            for (int i = 0; i < maxChunkCount; i++) {
                digest.reset();

                digest.update(Longs.toByteArray(chunksGenerated + i), 0, Longs.BYTES);

                digest.update(seed, 0, 64);

                digest.update(publicKey.getPublicKey(), 0, publicKey.getPublicKey().length);

                digest.doFinal(chunks[i], 0);
            }

            //solving
            for (int i = ids.length - 1; i >= 0; i--) {
                boolean mine = true;
                while (mine) {
                    //resetting
                    for (int j = 0; j < result.length; j++) {
                        result[j] = 0;
                    }

                    //generating solution
                    System.arraycopy(chunks[ids[0]], 0, result, 0, chunks[ids[0]].length);
                    for (int j = 1; j < ids.length; j++) {
                        for (int k = 0; k < chunks[ids[j]].length; k++) {
                            result[k] ^= chunks[ids[j]][k];
                        }
                    }

                    //checking solution
                    boolean isValid = true;
                    for (int j = 0; j < result.length; j++) {
                        isValid &= result[j] == 0;
                    }

                    //submitting solution
                    if (isValid) {
                        long[] finalIds = new long[n];
                        for (int j = 0; j < ids.length; j++) {
                            finalIds[j] = ids[j] + chunksGenerated;
                        }

                        return new Shard(finalIds, seed, publicKey);
                    }

                    //updating ids for next solution
                    if (i == (n - 1)) {
                        mine = false;
                    } else if (ids[i] == (ids[i + 1] - 1)) {
                        mine = false;
                    } else {
                        mine = false;
                        ids[i]++;
                    }
                }
            }


            chunksGenerated += maxChunkCount;
        }
        throw new Exception("Unable to compute result.");
    }
}
