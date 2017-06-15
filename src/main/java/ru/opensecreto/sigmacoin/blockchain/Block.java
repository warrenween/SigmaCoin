package ru.opensecreto.sigmacoin.blockchain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Block {

    private final BlockHeader header;

    private final List<byte[]> transactionHashes;

    public Block(BlockHeader header, List<byte[]> transactionHashes) {
        this.header = checkNotNull(header);

        checkNotNull(transactionHashes);
        this.transactionHashes = new ArrayList<>(transactionHashes.size());
        transactionHashes.forEach(bytes -> transactionHashes.add(Arrays.copyOf(bytes, bytes.length)));
    }
}
