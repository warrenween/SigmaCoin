package ru.opensecreto.sigmacoin.por.work;

import com.google.common.primitives.Longs;
import org.bouncycastle.crypto.Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.core.DigestProvider;
import ru.opensecreto.sigmacoin.crypto.Ed25519.Ed25519PublicKey;
import ru.opensecreto.sigmacoin.por.capacity.Shard;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class ShardMiner implements Callable<Shard> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShardMiner.class);

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

        LOGGER.info("Started mining.");
        long stepCounter = 0;
        long attemptsDone = 0;

        //check id of last of next chunks is less than 8 bit
        while (chunksGenerated < (Long.MAX_VALUE - maxChunkCount)) {
            LOGGER.debug("Started mining step {}.", stepCounter);

            for (int i = 0; i < n; i++) {
                ids[i] = i;
            }

            //generating chunk
            long chunkStart = System.nanoTime();
            for (int i = 0; i < maxChunkCount; i++) {
                digest.reset();

                digest.update(Longs.toByteArray(chunksGenerated + i), 0, Longs.BYTES);

                digest.update(seed, 0, 64);

                digest.update(publicKey.getPublicKey(), 0, publicKey.getPublicKey().length);

                digest.doFinal(chunks[i], 0);
            }

            long chunkElapsedNano = System.nanoTime() - chunkStart;
            LOGGER.trace("Generated {} chunks. Total {}sec. Time per one {}ns.",
                    maxChunkCount, TimeUnit.NANOSECONDS.toSeconds(chunkElapsedNano),
                    chunkElapsedNano / maxChunkCount);

            //solving
            long solvingNanoStart = System.nanoTime();
            for (int i = ids.length - 1; i >= 0; i--) {
                boolean mine = true;
                while (mine) {
                    //resetting
                    for (int j = 0; j < result.length; j++) {
                        result[j] = 0;
                    }

                    //generating solution
                    System.arraycopy(chunks[ids[0]], 0, result, 0, digestLength);
                    for (int j = 1; j < ids.length; j++) {
                        for (int k = 0; k < digestLength; k++) {
                            result[k] ^= chunks[ids[j]][k];
                        }
                    }

                    //checking solution
                    boolean isValid = true;
                    for (int j = 0; j < result.length; j++) {
                        isValid &= result[j] == 0;
                    }
                    attemptsDone++;

                    //submitting solution
                    if (isValid) {
                        long solvingNanoElapsed = System.nanoTime() - solvingNanoStart;
                        LOGGER.info("Found valid shard. Yay!!! Attempts done {}. Chunks generated {}. Time per solution {}ns.",
                                attemptsDone + 1, chunksGenerated + maxChunkCount, solvingNanoElapsed / attemptsDone);
                        long[] finalIds = new long[n];
                        for (int j = 0; j < ids.length; j++) {
                            finalIds[j] = ids[j] + chunksGenerated;
                        }

                        return new Shard(finalIds, seed, publicKey);
                    }

                    //updating ids for next solution
                    if (i == (n - 1)) {
                        if (ids[i] == (maxChunkCount - 1)) {
                            mine = false;
                        } else {
                            ids[i]++;
                        }
                    } else {
                        if (ids[i] == (ids[i + 1] - 1)) {
                            mine = false;
                        } else {
                            ids[i]++;
                        }
                    }

                }
            }

            LOGGER.debug("Step {} was not lucky. Chunks processed {}. Attempts done {}",
                    stepCounter, chunksGenerated += maxChunkCount, attemptsDone);
            chunksGenerated += maxChunkCount;
            stepCounter++;
        }
        throw new Exception("Unable to compute result.");
    }
}
