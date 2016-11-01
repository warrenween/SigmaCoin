package org.opensecreto.TheGreatBlockchainArchive;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Important: controller is NOT thread safe, also it is NOT protected from corrupted index and blockchain files
 */
public class BlockchainArchiveController {

    public BlockchainArchiveConfiguration configuration;
    private RandomAccessFile index;
    private RandomAccessFile blockchain;

    public BlockchainArchiveController(BlockchainArchiveConfiguration configuration) throws IOException {
        this.configuration = configuration;
        configuration.setImmutable();

        index = new RandomAccessFile(configuration.getIndexFile(), "rwd");
        blockchain = new RandomAccessFile(configuration.getBlockchainFile(), "rwd");
    }

    /**
     * @param hash String representing hex hash
     * @return String data if found or null if not found.
     */
    public String get(String hash) throws IOException {
        return this.get(DatatypeConverter.parseHexBinary(hash));
    }

    public String get(byte[] hash) throws IOException {
        BlockIndex index = findHash(hash);
        if (index == null) {
            return null;
        } else {
            blockchain.seek(index.offset);
            byte[] data = new byte[index.length];
            blockchain.read(data, 0, index.length);
            return new String(data);
        }
    }

    /**
     * @param hash Hex string representation of hash
     * @see BlockchainArchiveController#delete(byte[])
     */
    public boolean delete(String hash) throws IOException {
        return this.delete(DatatypeConverter.parseHexBinary(hash));
    }

    /**
     * Deletes block with given hash.
     * Notice: block is not actually deleted! Given index in index file is marked as invalid, though it
     * can be overwritten. Data in blockchain file is not deleted.
     * TODO: clean blockchain file from deleted content
     *
     * @param hash
     * @return True if delete was successful, otherwise false
     */
    public boolean delete(byte[] hash) throws IOException {
        BlockIndex index = findHash(hash);
        if (index == null) {
            return false;
        } else {
            this.index.seek(index.indexOffset);
            this.index.writeBoolean(false);
            return true;
        }
    }

    /**
     * @param hash String representing hex hash
     */
    public void put(String hash, String data) throws IOException {
        this.put(DatatypeConverter.parseHexBinary(hash), data);
    }

    public void put(byte[] hash, String data) throws IOException {
        if (hash.length != configuration.getHashLength()) {
            throw new IllegalArgumentException("Given hash length " + hash.length + " " +
                    "is not equal to configuration length " + configuration.getHashLength() + ".");
        }

        //searching for free space in index file
        int indexOffset = (int) index.length();
        index.seek(0);
        boolean search = true;
        while ((index.getFilePointer() < index.length()) && search) {
            int currentOffset = (int) index.getFilePointer();
            if (!index.readBoolean()) {
                search = false;
                //we read one byte with index.readBoolean() that's why we should do -1 indexGetFilePointer
                indexOffset = currentOffset;
            }
            index.skipBytes(configuration.getHashLength());
            //int offset and int length
            index.skipBytes(Integer.BYTES * 2);
        }

        int blockchainOffset = (int) blockchain.length();
        blockchain.seek(blockchainOffset);
        blockchain.write(data.getBytes());

        this.index.seek(indexOffset);
        this.index.writeBoolean(true);
        this.index.write(hash);
        this.index.writeInt(blockchainOffset);
        this.index.writeInt(data.getBytes().length);
    }

    private BlockIndex findHash(byte[] hash) throws IOException {
        index.seek(0);
        byte[] tempHash = new byte[configuration.getHashLength()];
        while (index.getFilePointer() < index.length()) {
            int indexOffset = (int) index.getFilePointer();
            boolean valid = index.readBoolean();
            index.read(tempHash);
            int offset = index.readInt();
            int length = index.readInt();

            if (Arrays.equals(tempHash, hash) && valid) {
                return new BlockIndex(hash, offset, length, indexOffset);
            }
        }
        return null;
    }

    private static class BlockIndex {
        public final byte[] hash;
        public final int offset;
        public final int length;
        public final int indexOffset;

        public BlockIndex(byte[] hash, int offset, int length, int indexOffset) {
            this.hash = hash;
            this.offset = offset;
            this.length = length;
            this.indexOffset = indexOffset;
        }
    }

}
