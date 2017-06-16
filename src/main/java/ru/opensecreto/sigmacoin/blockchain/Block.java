package ru.opensecreto.sigmacoin.blockchain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.config.BaseConfig;
import ru.opensecreto.sigmacoin.core.DigestProvider;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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

    public static final Logger LOGGER = LoggerFactory.getLogger(Block.class);

    public static byte[] encode(Block block) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOut = new DataOutputStream(out);
        try {
            byte[] headerData = FullBlockHeader.encode(block.fullBlockHeader);
            dataOut.writeInt(headerData.length);
            out.write(headerData);

            dataOut.writeInt(block.transactionHashes.size());
            for (byte[] transactionHash : block.transactionHashes) {
                out.write(transactionHash);
            }
        } catch (IOException e) {
            LOGGER.warn("Something unexpected happened when encoding block.", e);
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }

    public static Block decode(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        byte[] fullBlockHeaderData = new byte[buffer.getInt()];
        buffer.get(fullBlockHeaderData);
        FullBlockHeader fullBlockHeader = FullBlockHeader.decode(fullBlockHeaderData);

        int txCount = buffer.getInt();
        List<byte[]> txHashes = new ArrayList<>(txCount);
        for (int i = 0; i < txCount; i++) {
            byte[] hash = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
            buffer.get(hash);
            txHashes.add(hash);
        }
        return new Block(fullBlockHeader, txHashes);
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof Block)) {
            return false;
        }
        Block block = (Block) obj;
        return block.fullBlockHeader.equals(fullBlockHeader) && block.transactionHashes.equals(transactionHashes);
    }
}
