package ru.opensecreto.sigmacoin.blockchain;

import ru.opensecreto.sigmacoin.core.DigestProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class Block {

    private final FullBlockHeader fullBlockHeader;

    private final List<byte[]> transactionHashes;

    public Block(FullBlockHeader fullBlockHeader, List<byte[]> transactionHashes) {
        this.fullBlockHeader = checkNotNull(fullBlockHeader);

        checkNotNull(transactionHashes);
        this.transactionHashes = new ArrayList<>(transactionHashes.size());
        transactionHashes.forEach(bytes -> transactionHashes.add(Arrays.copyOf(bytes, bytes.length)));
    }

    public byte[] getBlockHash(DigestProvider provider) {
        return fullBlockHeader.getHash(provider);
    }
}
