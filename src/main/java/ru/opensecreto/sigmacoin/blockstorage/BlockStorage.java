package ru.opensecreto.sigmacoin.blockstorage;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Important: controller is NOT thread safe, also it is NOT protected from corrupted index and blockstorage files
 */
public class BlockStorage {

    public BlockStorageConfiguration configuration;
    private RandomAccessFile index;

    public BlockStorage(BlockStorageConfiguration configuration) throws IOException {
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
