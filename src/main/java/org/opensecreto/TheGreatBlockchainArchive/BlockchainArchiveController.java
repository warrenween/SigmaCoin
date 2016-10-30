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

        index = new RandomAccessFile(configuration.getPath() + "index.data", "rwd");
        blockchain = new RandomAccessFile(configuration.getPath() + "blockchain.dat", "rwd");
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
        index.seek(0);
        byte[] tempHash = new byte[configuration.getHashLength()];
        long line;
        while (index.getFilePointer() < index.length()) {
            index.read(tempHash);
            long offset = index.readLong();
            long length = index.readLong();

            if (Arrays.equals(hash, hash)) {
                byte[] bytes = new byte[(int) length];
                blockchain.readFully(bytes, (int) offset, (int) length);
                return new String(bytes);
            }
        }
        return null;
    }

    /**
     * @param key  String representing hex hash
     * @param data
     */
    public void put(String key, String data) {

    }

}
