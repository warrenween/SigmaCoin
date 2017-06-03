package ru.opensecreto.sigmacoin.por.work;

import com.google.common.primitives.Ints;
import org.bouncycastle.crypto.Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.core.DigestProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
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
        //constants
        int chunkSize = chunkProvider.getDigest().getDigestSize();
        int hashSize = hashProvider.getDigest().getDigestSize();

        //counters
        long roundCounter = 1;
        BigInteger solvingAttemptsDoneTotal = BigInteger.ZERO;
        BigInteger memhashesSuccessfulTotal = BigInteger.ZERO;
        //chunkCounter is negative to cover 100% of integer values
        //ints are signed so if we start with 0 we will cover 1/2 ov all possible chunk values
        int chunkCounter = Integer.MIN_VALUE;
        long chunksGenerated = 0;
        long solvingTimeNanosTotal = 0;

        //used to check chunkCounter overflow
        int chunkMinId = Integer.MIN_VALUE;
        int chunkMaxId = Integer.MIN_VALUE + maxChunkCount - 1;

        //store ids for selecting chunks from chunks array
        //modify this values to select new chunks set
        int[] ids = new int[n];
        //store chunks
        byte[][] chunks = new byte[maxChunkCount][chunkSize];
        //map each chunk to its id
        int[] chunkIds = new int[maxChunkCount];

        //needed stuff
        Digest chunkDigest = chunkProvider.getDigest();
        Digest hashDigest = hashProvider.getDigest();
        byte[] chunkResult = new byte[chunkSize];
        byte[] hashResult = new byte[hashSize];

        LOGGER.info("Successfully prepared. Starting mining.");

        //by default chunkMaxId>chunksMinId
        //but if chunkMaxId has oveflow it will be chunkMaxId<chunkMinId and mining stops
        while (chunkMaxId > chunkMinId) {
            LOGGER.debug("Started mining round {}.", roundCounter);
            //setting default ids
            for (int i = 0; i < ids.length; i++) {
                ids[i] = i;
            }

            //generating chunks
            long chunkTimeNanoStart = System.nanoTime();
            for (int i = 0; i < maxChunkCount; i++) {
                chunksGenerated++;
                chunkDigest.reset();

                chunkDigest.update(Ints.toByteArray(chunkCounter), 0, Integer.BYTES);
                chunkDigest.update(data, 0, data.length);

                chunkDigest.doFinal(chunks[i], 0);
                chunkIds[i] = chunkCounter;
                chunkCounter++;
            }
            long chunkTimeNanoElapsed = System.nanoTime() - chunkTimeNanoStart;
            LOGGER.trace("Generated {} chunks. Time for all chunks {}. Time per one chunk {}.",
                    maxChunkCount, Duration.ofNanos(chunkTimeNanoElapsed),
                    Duration.ofNanos(chunkTimeNanoElapsed / maxChunkCount));


            //solving
            long solvingRoundNanoTimeStart = System.nanoTime();
            BigInteger solvingAttemptsDoneInRound = BigInteger.ZERO;
            BigInteger memhashesSuccessfulInRound = BigInteger.ZERO;
            boolean mine = true;
            while (mine) {
                solvingAttemptsDoneInRound = solvingAttemptsDoneInRound.add(BigInteger.ONE);

                //resetting result cache
                for (int i = 0; i < chunkResult.length; i++) {
                    chunkResult[i] = 0;
                }

                //xoring chunks
                for (int id : ids) {
                    for (int i = 0; i < chunkSize; i++) {
                        chunkResult[i] ^= chunks[id][i];
                    }
                }

                //checking solution
                boolean isValid = true;
                for (byte aResult : chunkResult) {
                    isValid &= aResult == 0;
                }

                //checking hash meets target
                if (isValid) {
                    memhashesSuccessfulInRound = memhashesSuccessfulInRound.add(BigInteger.ONE);

                    //mapping ids to chunkIds
                    long[] tmpChunkIds = new long[n];
                    for (int i = 0; i < n; i++) {
                        tmpChunkIds[i] = chunkIds[ids[i]];
                    }

                    //converting to longs
                    for (int i = 0; i < n; i++) {
                        tmpChunkIds[i] = tmpChunkIds[i] & 0xffffffffL;
                    }

                    //sorting
                    Arrays.sort(tmpChunkIds);

                    //casting tmpChunkIds back to ints
                    int[] finalChunkIds = new int[n];
                    for (int i = 0; i < n; i++) {
                        finalChunkIds[i] = (int) tmpChunkIds[i];
                    }

                    hashDigest.reset();
                    hashDigest.update(data, 0, data.length);
                    for (int j = 0; j < n; j++) {
                        hashDigest.update(Ints.toByteArray(finalChunkIds[j]), 0, Integer.BYTES);
                    }
                    hashDigest.doFinal(hashResult, 0);

                    //checking if hash is less than header
                    if (meetsTarget(hashResult, target)) {
                        long solvingRoundNanoTime = System.nanoTime() - solvingRoundNanoTimeStart;
                        solvingAttemptsDoneTotal = solvingAttemptsDoneTotal.add(solvingAttemptsDoneInRound);
                        memhashesSuccessfulTotal = memhashesSuccessfulTotal.add(memhashesSuccessfulInRound);
                        LOGGER.info(
                                "Found valid solution!!! " +
                                        "Attempts total {}. " +
                                        "Successful memhashes total {}. Successful memhashes rate total {}%. " +
                                        "Total chunks processed {}." +
                                        "Total time {}. " +
                                        "Time per solution {}.",
                                solvingAttemptsDoneTotal,
                                memhashesSuccessfulTotal, getRate(memhashesSuccessfulTotal, solvingAttemptsDoneTotal).toPlainString(),
                                chunksGenerated,
                                Duration.ofNanos(solvingRoundNanoTime),
                                Duration.ofNanos(BigInteger.valueOf(solvingRoundNanoTime).divide(solvingAttemptsDoneTotal).longValue()));
                        return finalChunkIds;
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

            long solvingRoundNanoTime = System.nanoTime() - solvingRoundNanoTimeStart;
            solvingTimeNanosTotal += solvingRoundNanoTime;
            solvingAttemptsDoneTotal = solvingAttemptsDoneTotal.add(solvingAttemptsDoneInRound);
            memhashesSuccessfulTotal = memhashesSuccessfulTotal.add(memhashesSuccessfulInRound);
            LOGGER.debug(
                    "Round {} was unlucky. " +
                            "Attempts in round {}. Attempts total {}. " +
                            "Successful memhashes in round {}. Successful memhases rate in round {}%. " +
                            "Successful memhashes total {}. Successful memhashes rate total {}%. " +
                            "Total chunks processed {}. " +
                            "Time per round {}. Total time {}. " +
                            "Time per solution {}.",
                    roundCounter,
                    solvingAttemptsDoneInRound, solvingAttemptsDoneTotal,
                    memhashesSuccessfulInRound, getRate(memhashesSuccessfulInRound, solvingAttemptsDoneInRound).toPlainString(),
                    memhashesSuccessfulTotal, getRate(memhashesSuccessfulTotal, solvingAttemptsDoneTotal).toPlainString(),
                    chunksGenerated,
                    Duration.ofNanos(solvingRoundNanoTime), Duration.ofNanos(solvingTimeNanosTotal),
                    Duration.ofNanos(BigInteger.valueOf(solvingRoundNanoTime).divide(solvingAttemptsDoneInRound).longValue()));
            chunkMinId += maxChunkCount;
            chunkMaxId += maxChunkCount;
            roundCounter++;
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

    private BigDecimal getRate(BigInteger good, BigInteger total) {
        return new BigDecimal(good, new MathContext(8))
                .multiply(BigDecimal.valueOf(100))
                .divide(new BigDecimal(total, new MathContext(8)), BigDecimal.ROUND_DOWN);
    }
}

