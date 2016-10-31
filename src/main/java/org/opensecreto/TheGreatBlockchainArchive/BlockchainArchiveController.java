package org.opensecreto.TheGreatBlockchainArchive;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

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
     * @throws IOException
     */
    public String get(String hash) throws IOException {
        return this.get(DatatypeConverter.parseHexBinary(hash));
    }

    public String get(byte[] hash) throws IOException {
        BlockIndex index = findHash(hash);
        if (index == null) {
            return null;
        } else {
            byte[] data = new byte[index.length];
            blockchain.readFully(data, index.offset, index.length);
            return new String(data);
        }
    }

    /**
     * @param hash String representing hex hash
     * @param data
     */
    public void put(String hash, String data) throws IOException {
        this.put(DatatypeConverter.parseHexBinary(hash), data);
    }

    public void put(byte[] hash, String data) throws IOException {
        if (hash.length != configuration.getHashLength()) {
            throw new IllegalArgumentException("Given hash length " + hash.length + " " +
                    "is not equal to configuration length " + configuration.getHashLength() + ".");
        }

        blockchain.seek(blockchain.length() - 1);
        int index = (int) blockchain.getFilePointer();
        blockchain.write(data.getBytes());

        this.index.seek(configuration.getHashLength() - 1);
        this.index.write(hash);
        this.index.write(index);
        this.index.write(data.getBytes().length);
    }

    private BlockIndex findHash(byte[] hash) throws IOException {
        index.seek(0);
        byte[] tempHash = new byte[configuration.getHashLength()];
        long line;
        while (index.getFilePointer() < index.length()) {
            index.read(tempHash);
            int offset = index.readInt();
            int length = index.readInt();

            if (Arrays.equals(tempHash, hash)) {
                byte[] bytes = new byte[(int) length];
                blockchain.readFully(bytes, (int) offset, (int) length);
                return new BlockIndex(hash, offset, length);
            }
        }
        return null;
    }

    private static class BlockIndex {
        public final byte[] hash;
        public final int offset;
        public final int length;

        public BlockIndex(byte[] hash, int offset, int length) {
            this.hash = hash;
            this.offset = offset;
            this.length = length;
        }
    }

}
