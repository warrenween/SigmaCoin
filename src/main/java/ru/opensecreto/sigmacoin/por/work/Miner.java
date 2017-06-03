package ru.opensecreto.sigmacoin.por.work;

import com.google.common.primitives.Ints;
import org.bouncycastle.crypto.Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.core.DigestProvider;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class Miner implements Callable<int[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Miner.class);

    private final int maxChunkCount;
    private final byte[] data;
    private final DigestProvider chunkProvider;
    private final DigestProvider hashProvider;
    private final int n;
    private final byte[] target;

    public Miner(byte[] data, DigestProvider chunkProvider, DigestProvider hashProvider, int n, byte[] target, int maxChunkCount) {
        if (data == null) throw new IllegalArgumentException("data can not be null");
        if (chunkProvider == null) throw new IllegalArgumentException("Chunk digest provider can not be null.");
        if (hashProvider == null) throw new IllegalArgumentException("Hash digest provider can not be null.");
        if (target == null) throw new IllegalArgumentException("target can not be null");

        if (hashProvider.getDigest() == null)
            throw new IllegalArgumentException("Hash digest provider can not return null");
        if (chunkProvider.getDigest() == null)
            throw new IllegalArgumentException("Chunk digest provider can not return null");
        if (n < 2) throw new IllegalArgumentException("N must be >= 2");
        if (hashProvider.getDigest().getDigestSize() != target.length)
            throw new IllegalArgumentException("Target bytes count is not equal to digest size.");
        if (maxChunkCount < n) throw new IllegalArgumentException("Max chunk count can not be less than N.");

        this.data = Arrays.copyOf(data, data.length);
        this.chunkProvider = chunkProvider;
        this.hashProvider = hashProvider;
        this.n = n;
        this.target = Arrays.copyOf(target, target.length);
        this.maxChunkCount = maxChunkCount;
    }

    @Override
    public int[] call() throws Exception {
        //counters
        int chunkSize = chunkProvider.getDigest().getDigestSize();
        long stepCounter = 0;
        BigInteger attemptsDone = BigInteger.ZERO;
        int chunkCounter = Integer.MIN_VALUE;
        long chunksGenerated = 0;
        //values are negative to cover 100% of integer values
        //ints are signed so if we start with 0 we will cover 1/2 ov all possible values

        int chunkMinId = Integer.MIN_VALUE;
        int chunkMaxId = Integer.MIN_VALUE + maxChunkCount - 1;

        //store ids for selecting chunks from chunks array
        //modify this values to select new chunks set
        int[] ids = new int[n];
        //store chunks
        byte[][] chunks = new byte[maxChunkCount][chunkSize];
        //map each chunk to its id
        int[] chunkId = new int[maxChunkCount];
        int[] finalIds = new int[n];

        //needed stuff
        Digest chunkDigest = chunkProvider.getDigest();
        Digest hashDigest = hashProvider.getDigest();
        byte[] result = new byte[chunkSize];

        LOGGER.info("Successfully prepared. Starting mining.");

        //checking chunkMaxId has already overflew
        while (chunkMaxId > chunkMinId) {
            LOGGER.debug("Started mining step {}.", stepCounter);

            //generating chunks
            long chunkTimeNanoStart = System.nanoTime();

            for (int i = 0; i < maxChunkCount; i++) {
                chunksGenerated++;
                chunkDigest.reset();

                chunkDigest.update(Ints.toByteArray(chunkCounter), 0, Integer.BYTES);
                chunkDigest.update(data, 0, data.length);

                chunkDigest.doFinal(chunks[i], 0);
                chunkId[i] = chunkCounter;
                chunkCounter++;
            }

            long chunkTimeNanoElapsed = System.nanoTime() - chunkTimeNanoStart;
            LOGGER.trace("Generated {} chunks. Total {}. Time per one {}ns.",
                    maxChunkCount, Duration.ofNanos(chunkTimeNanoElapsed),
                    chunkTimeNanoElapsed / maxChunkCount);

            for (int i = 0; i < ids.length; i++) {
                ids[i] = i;
            }

            //solving
            long solvingTimeNanoStart = System.nanoTime();

            for (int i = ids.length - 1; i >= 0; i--) {
                boolean mine = true;

                while (mine) {
                    //resetting result cache
                    for (int j = 0; j < result.length; j++) {
                        result[j] = 0;
                    }

                    //xoring chunks
                    System.arraycopy(chunks[ids[0]], 0, result, 0, chunkSize);
                    for (int j = 1; j < ids.length; j++) {
                        for (int k = 0; k < chunkSize; k++) {
                            result[k] ^= chunks[ids[j]][k];
                        }
                    }

                    //checking solution
                    boolean isValid = true;
                    for (byte aResult : result) {
                        isValid &= aResult == 0;
                    }
                    attemptsDone = attemptsDone.add(BigInteger.ONE);

                    //checking final
                    if (isValid) {
                        //sorting unsigned
                        long[] tmp = new long[n];
                        for (int j = 0; j < n; j++) {
                            tmp[j] = ids[j] & 0xffffffffL;
                        }
                        Arrays.sort(tmp);
                        int[] tmpInt = new int[n];
                        for (int j = 0; j < n; j++) {
                            tmpInt[j] = (int) tmp[j];
                        }

                        hashDigest.reset();
                        hashDigest.update(data, 0, data.length);
                        for (int j = 0; j < finalIds.length; j++) {
                            hashDigest.update(Ints.toByteArray(tmpInt[j]), 0, Integer.BYTES);
                        }
                        hashDigest.doFinal(result, 0);

                        //checking if hash is less than header
                        if (meetsTarget(result, target)) {
                            long solvingNanoElapsed = System.nanoTime() - solvingTimeNanoStart;
                            LOGGER.info("Found valid solution. Attempts done {}. Chunks generated {}. Attempts done {}." +
                                            "Total time {}. Time per solution {}ns.",
                                    attemptsDone, chunksGenerated, attemptsDone, Duration.ofNanos(solvingNanoElapsed),
                                    BigInteger.valueOf(solvingNanoElapsed).divide(attemptsDone));
                            return tmpInt;
                        }
                    }

                    //updating ids for next solution
                    int j = 0;
                    boolean update = true;
                    while ((j < (n)) & update) {
                        if (j == (n - 1)) {
                            if (ids[j] == maxChunkCount - 1) {
                                mine = false;
                            } else {
                                ids[j]++;
                            }
                        } else {
                            if ((ids[j + 1] - ids[j]) > 1) {
                                ids[j]++;
                                update = false;
                            }
                        }
                        j++;
                    }
                }
            }

            LOGGER.debug("Step {} was not lucky. Chunks processed {}. Attempts done {}",
                    stepCounter, chunksGenerated, attemptsDone);
            chunkMinId += maxChunkCount;
            chunkMaxId += maxChunkCount;
            stepCounter++;
        }
        LOGGER.info("Unable to find solution.");
        throw new Exception("Unable to compute result.");
    }

    private boolean meetsTarget(byte[] hash, byte[] target) {
        for (int i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) > (target[i] & 0xff)) {
                return false;
            }
            if ((hash[i] & 0xff) < (target[i] & 0xff)) {
                return true;
            }
        }
        return true;
    }
}

