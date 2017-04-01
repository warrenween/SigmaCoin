package ru.opensecreto.sigmacoin.blockstorage;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;

public class BlockStorageController {

    public static final Logger LOGGER = LoggerFactory.getLogger(BlockStorageController.class);

    private final int hashSize;
    private final int maxBlockSize;

    private DB db;
    private HTreeMap<byte[], byte[]> blocks;

    public BlockStorageController(File file, int hashSize, int maxBlockSize) {
        this.hashSize = hashSize;
        this.maxBlockSize = maxBlockSize;

        db = DBMaker.fileDB(file).transactionEnable().closeOnJvmShutdown().make();
        blocks = db.hashMap("blocks", Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY)
                .counterEnable().createOrOpen();
    }

    public boolean addBlock(byte[] hash, byte[] data) {
        checkHash(hash);
        checkBlock(data);
        if (!blocks.containsKey(hash)) {
            blocks.put(hash, data);
            return true;
        }
        return false;
    }

    public boolean hasBlock(byte[] hash) {
        checkHash(hash);
        return blocks.containsKey(hash);
    }

    public byte[] getBlock(byte[] hash) {
        checkHash(hash);
        return blocks.get(hash);
    }

    private void checkHash(byte[] hash) {
        if (hash.length != hashSize) throw new IllegalArgumentException(
                "Incorrect hash length. Required " + hashSize + " bytes. Got " + hash.length + " bytes."
        );
    }

    private void checkBlock(byte[] data) {
        if (data.length > maxBlockSize) throw new IllegalArgumentException(
                "Block is too big. Max " + maxBlockSize + " bytes. Given " + data.length + " bytes."
        );
    }

    public void close() {
        blocks.close();
        db.close();
    }

}
