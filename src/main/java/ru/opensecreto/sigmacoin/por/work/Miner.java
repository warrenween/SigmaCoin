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
import java.util.concurrent.TimeUnit;

public class Miner implements Callable<int[]> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Miner.class);

    private final int maxChunkCount;
    private final byte[] data;
    private final DigestProvider digestProvider;
    private final int n;
    private final byte[] target;

    public Miner(byte[] data, DigestProvider digestProvider, int n, byte[] target, int maxChunkCount) {
        if (data == null) throw new IllegalArgumentException("data can not be null");
        if (digestProvider == null) throw new IllegalArgumentException("Digest provider can not be null.");
        if (target == null) throw new IllegalArgumentException("target can not be null");

        if (digestProvider.getDigest() == null)
            throw new IllegalArgumentException("Digest provider can not return null");
        if (n < 2) throw new IllegalArgumentException("N must be >= 2");
        if (digestProvider.getDigest().getDigestSize() != target.length)
            throw new IllegalArgumentException("Target bytes count is not equal to digest size.");
        if (maxChunkCount < n) throw new IllegalArgumentException("Max chunk count can not be less than N.");

        this.data = Arrays.copyOf(data, data.length);
        this.digestProvider = digestProvider;
        this.n = n;
        this.target = Arrays.copyOf(target, target.length);
        this.maxChunkCount = maxChunkCount;
    }

    @Override
    public int[] call() throws Exception {
        //counters
        int digestLength = digestProvider.getDigest().getDigestSize();
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
        byte[][] chunks = new byte[maxChunkCount][digestLength];
        //map each chunk to its id
        int[] chunkId = new int[maxChunkCount];
        int[] finalIds = new int[n];

        //needed stuff
        Digest digest = digestProvider.getDigest();
        byte[] result = new byte[digestLength];

        LOGGER.info("Successfully prepared. Starting mining.");

        //checking chunkMaxId has already overflew
        while (chunkMaxId > chunkMinId) {
            LOGGER.debug("Started mining step {}.", stepCounter);

            //generating chunks
            long chunkTimeNanoStart = System.nanoTime();

            for (int i = 0; i < maxChunkCount; i++) {
                chunksGenerated++;
                digest.reset();

                digest.update(Ints.toByteArray(chunkCounter), 0, Integer.BYTES);
                digest.update(data, 0, data.length);

                digest.doFinal(chunks[i], 0);
                chunkId[i] = chunkCounter;
                chunkCounter++;
            }

            long chunkTimeNanoElapsed = System.nanoTime() - chunkTimeNanoStart;
            LOGGER.trace("Generated {} chunks. Total {}sec. Time per one {}ns.",
                    maxChunkCount, TimeUnit.NANOSECONDS.toSeconds(chunkTimeNanoElapsed),
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
                    attemptsDone = attemptsDone.add(BigInteger.ONE);

                    //checking final
                    boolean solutionFound = false;
                    if (isValid) {
                        LOGGER.trace("Found valid MemHash solution.");
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

                        digest.reset();
                        digest.update(data, 0, data.length);
                        for (int j = 0; j < finalIds.length; j++) {
                            digest.update(Ints.toByteArray(j), 0, Integer.BYTES);
                        }
                        digest.doFinal(result, 0);

                        //checking if hash is less than header
                        boolean search = true;
                        int j = 0;
                        while ((j < result.length) & search) {
                            if ((result[j] & 0xff) > (target[j] & 0xff)) {
                                search = false;
                                solutionFound = false;
                            }
                            if ((result[j] & 0xff) < (target[j] & 0xff)) {
                                search = false;
                                solutionFound = true;
                            }

                            j++;
                        }
                    }

                    if (solutionFound) {
                        long solvingNanoElapsed = System.nanoTime() - solvingTimeNanoStart;
                        LOGGER.info("Found valid solution. Attempts done {}. Chunks generated {}. " +
                                        "Total time {}. Time per solution {}ns.",
                                attemptsDone, chunksGenerated, attemptsDone, Duration.ofNanos(solvingNanoElapsed),
                                BigInteger.valueOf(solvingNanoElapsed).divide(attemptsDone));

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
        LOGGER.info("Unable to find solution.");
        throw new Exception("Unable to compute result.");
    }
}
