package ru.opensecreto.sigmacoin.blockstorage;

import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.env.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class BlockStorageController {

    public static final Logger LOGGER = LoggerFactory.getLogger(BlockStorageController.class);

    private final int hashSize;

    private final Environment environment;
    private final Store blockStore;

    public BlockStorageController(File dbDir, int hashSize) {
        checkNotNull(dbDir);

        this.hashSize = hashSize;

        environment = Environments.newInstance(dbDir, new EnvironmentConfig().setLogDurableWrite(true));

        blockStore = environment.computeInTransaction(
                txn -> environment.openStore("blocks", StoreConfig.WITHOUT_DUPLICATES, txn, true)
        );
    }

    public boolean addBlock(byte[] hash, byte[] data)
            throws NullPointerException, IllegalArgumentException {
        checkNotNull(hash);
        checkNotNull(data);
        checkArgument(hash.length == hashSize);
        return environment.computeInTransaction(
                txn -> blockStore.add(txn, new ArrayByteIterable(hash), new ArrayByteIterable(data))
        );
    }

    public boolean hasBlock(byte[] hash)
            throws NullPointerException, IllegalArgumentException {
        checkNotNull(hash);
        checkArgument(hash.length == hashSize);
        return environment.computeInReadonlyTransaction(txn -> blockStore.get(txn, new ArrayByteIterable(hash)) != null);
    }

    public byte[] getBlock(byte[] hash) {
        checkNotNull(hash);
        checkArgument(hash.length == hashSize);
        return environment.computeInReadonlyTransaction(txn -> {
            if (!hasBlock(hash)) throw new IllegalStateException("Can not find block");
            return blockStore.get(txn, new ArrayByteIterable(hash)).getBytesUnsafe();
        });
    }

    public void close() {
        environment.close();
    }

}
