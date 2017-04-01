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

    private DB db;
    private HTreeMap<byte[], byte[]> blocks;

    public BlockStorageController(File file, int hashSize, int maxBlockSize) {
        db = DBMaker.fileDB(file).transactionEnable().closeOnJvmShutdown().make();
        blocks = db.hashMap("blocks", Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY)
                .counterEnable().createOrOpen();
    }

    public boolean addBlock(byte[] hash, byte[] data) {
        if (!blocks.containsKey(hash)) {
            blocks.put(hash, data);
            return true;
        }
        return false;
    }

    public boolean hasBlock(byte[] hash) {
        return blocks.containsKey(hash);
    }

    public byte[] getBlock(byte[] hash) {
        return blocks.get(hash);
    }

}
