package ru.opensecreto.sigmacoin.blockchaintorage;

import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.env.*;
import org.bouncycastle.crypto.Digest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.blockchain.Transaction;
import ru.opensecreto.sigmacoin.core.DigestProvider;

import java.io.File;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class BlockchainStorageController {

    public static final Logger LOGGER = LoggerFactory.getLogger(BlockchainStorageController.class);

    private final int hashSize;

    private final Environment environment;
    private final Store blockStore;
    private final Store transactionStore;
    private final DigestProvider digestProvider;

    public BlockchainStorageController(File dbDir, int hashSize, DigestProvider digestProvider) {
        checkNotNull(dbDir);
        this.digestProvider = checkNotNull(digestProvider);

        this.hashSize = hashSize;

        environment = Environments.newInstance(dbDir, new EnvironmentConfig().setLogDurableWrite(true));

        blockStore = environment.computeInTransaction(
                txn -> environment.openStore("blocks", StoreConfig.WITHOUT_DUPLICATES, txn, true)
        );
        transactionStore = environment.computeInTransaction(
                txn -> environment.openStore("transactions", StoreConfig.WITHOUT_DUPLICATES, txn, true)
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

    public byte[] addTransaction(Transaction transaction)
            throws NullPointerException {
        checkNotNull(transaction);
        final byte[] txData = Transaction.encode(transaction);
        Digest digest = digestProvider.getDigest();
        byte[] hash = new byte[digest.getDigestSize()];
        digest.update(txData, 0, txData.length);
        digest.doFinal(hash, 0);
        environment.executeInTransaction((jetbrains.exodus.env.Transaction txn) -> {
            transactionStore.add(txn, new ArrayByteIterable(hash), new ArrayByteIterable(txData));
            txn.commit();
        });
        return Arrays.copyOf(hash, hash.length);
    }

    public boolean hasTransaction(byte[] hash)
            throws NullPointerException, IllegalArgumentException {
        checkNotNull(hash);
        checkArgument(digestProvider.getDigestSize() == hash.length);
        return environment.computeInReadonlyTransaction(txn ->
                transactionStore.get(txn, new ArrayByteIterable(hash)) != null
        );
    }

    public Transaction getTransaction(byte[] hash) {
        checkNotNull(hash);
        checkArgument(hash.length == digestProvider.getDigestSize());
        return Transaction.decode(environment.computeInReadonlyTransaction(txn -> {
            if (!hasTransaction(hash)) throw new IllegalStateException("Can not find transaction.");
            return transactionStore.get(txn, new ArrayByteIterable(hash)).getBytesUnsafe();
        }));
    }


    public void close() {
        environment.close();
    }

}
