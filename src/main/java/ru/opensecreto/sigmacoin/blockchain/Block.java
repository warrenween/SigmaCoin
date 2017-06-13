package ru.opensecreto.sigmacoin.blockchain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Block {

    private final BigInteger height;
    private final BigInteger timestamp;
    private final BigInteger difficulty;
    private final byte[] parentHash;
    private final byte[] txRootHash;

    private final int[] pow;

    private final List<byte[]> transactionHashes;

    public Block(BigInteger height, BigInteger timestamp, BigInteger difficulty, byte[] parentHash, byte[] txRootHash, int[] pow, List<byte[]> transactionHashes) {
        this.height = checkNotNull(height);
        this.timestamp = checkNotNull(timestamp);
        this.difficulty = checkNotNull(difficulty);
        this.parentHash = Arrays.copyOf(checkNotNull(parentHash), parentHash.length);
        this.txRootHash = Arrays.copyOf(checkNotNull(txRootHash), txRootHash.length);

        this.pow = checkNotNull(pow);

        checkNotNull(transactionHashes);
        this.transactionHashes = new ArrayList<>(transactionHashes.size());
        transactionHashes.forEach(bytes -> transactionHashes.add(Arrays.copyOf(bytes, bytes.length)));

        //------
        checkArgument(height.signum() >= 0);
        checkArgument(timestamp.signum() >= 0);
        checkArgument(difficulty.signum() > 0);
        checkArgument(pow.length >= 2);
    }
}
