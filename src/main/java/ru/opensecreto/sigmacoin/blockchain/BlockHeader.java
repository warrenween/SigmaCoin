package ru.opensecreto.sigmacoin.blockchain;

import java.math.BigInteger;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class BlockHeader {

    private final BigInteger height;
    private final BigInteger timestamp;
    private final BigInteger difficulty;
    private final byte[] parentHash;
    private final byte[] txRootHash;
    private final byte[] stateRootHash;
    private final int[] pow;

    public BlockHeader(BigInteger height, BigInteger timestamp, BigInteger difficulty, byte[] parentHash,
                       byte[] txRootHash, int[] pow, byte[] stateRootHash) {
        this.height = checkNotNull(height);
        this.timestamp = checkNotNull(timestamp);
        this.difficulty = checkNotNull(difficulty);
        this.parentHash = Arrays.copyOf(checkNotNull(parentHash), parentHash.length);
        this.stateRootHash = Arrays.copyOf(checkNotNull(stateRootHash), stateRootHash.length);
        this.txRootHash = Arrays.copyOf(checkNotNull(txRootHash), txRootHash.length);

        this.pow = checkNotNull(pow);

        //------
        checkArgument(height.signum() >= 0);
        checkArgument(timestamp.signum() >= 0);
        checkArgument(difficulty.signum() > 0);
        checkArgument(pow.length >= 2);
    }

}
