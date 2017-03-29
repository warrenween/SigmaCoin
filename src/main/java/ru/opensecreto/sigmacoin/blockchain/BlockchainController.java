package ru.opensecreto.sigmacoin.blockchain;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Important: controller is NOT thread safe, also it is NOT protected from corrupted index and blockchain files
 */
public class BlockchainController {

    public BlockchainConfiguration configuration;
    private RandomAccessFile index;

    public BlockchainController(BlockchainConfiguration configuration) throws IOException {
        this.configuration = configuration;
    }

    public byte[] get(byte[] hash) throws IOException {
        return null;
    }


    public boolean delete(byte[] hash) throws IOException {
        return false;
    }

    public void put(byte[] hash, byte[] data) throws IOException {

    }

    private BlockIndex findHash(byte[] hash) throws IOException {
        return null;
    }


    public void reindex() throws IOException {

    }

    private static class BlockIndex {
        public final byte[] hash;
        public final long offset;
        public final int length;
        public final long indexOffset;

        public BlockIndex(byte[] hash, long offset, int length, long indexOffset) {
            this.hash = hash;
            this.offset = offset;
            this.length = length;
            this.indexOffset = indexOffset;
        }
    }

}
