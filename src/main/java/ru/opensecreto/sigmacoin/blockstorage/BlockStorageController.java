package ru.opensecreto.sigmacoin.blockstorage;

import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.env.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class BlockStorageController {

    public static final Logger LOGGER = LoggerFactory.getLogger(BlockStorageController.class);

    private final int hashSize;
    private final int maxBlockSize;

    private final Environment environment;
    private final Store blockStore;

    public BlockStorageController(File dbDir, int hashSize, int maxBlockSize, BlockStorageConfiguration configuration) {
        this.hashSize = hashSize;
        this.maxBlockSize = maxBlockSize;

        environment = Environments.newInstance(dbDir, new EnvironmentConfig().setLogDurableWrite(true));

        blockStore = environment.computeInTransaction(
                txn -> environment.openStore("blocks", StoreConfig.WITHOUT_DUPLICATES, txn, true)
        );
    }

    public boolean addBlock(byte[] hash, byte[] data) {
        checkHash(hash);
        checkBlock(data);
        environment.executeInTransaction(txn -> {
            blockStore.add(txn, new ArrayByteIterable(hash), new ArrayByteIterable(data));
        });
        return false;
    }

    public boolean hasBlock(byte[] hash) {
        checkHash(hash);
        return environment.computeInReadonlyTransaction(txn -> blockStore.get(txn, new ArrayByteIterable(hash)) != null);
    }

    public byte[] getBlock(byte[] hash) {
        checkHash(hash);
        return environment.computeInReadonlyTransaction(txn -> {
            if (!hasBlock(hash)) throw new IllegalStateException("Can not find block");
            return blockStore.get(txn, new ArrayByteIterable(hash)).getBytesUnsafe();
        });
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
        environment.close();
    }

}
