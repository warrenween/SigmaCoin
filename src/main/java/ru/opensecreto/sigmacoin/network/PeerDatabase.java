package ru.opensecreto.sigmacoin.network;

import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.PersistentEntityStore;
import jetbrains.exodus.entitystore.PersistentEntityStores;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.EnvironmentConfig;
import jetbrains.exodus.env.Environments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PeerDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerDatabase.class);

    private final Environment environment;
    private final PersistentEntityStore entityStore;

    public PeerDatabase(File dbDir) throws IOException {
        environment = Environments.newInstance(dbDir, new EnvironmentConfig().setLogDurableWrite(true));
        entityStore = PersistentEntityStores.newInstance(environment, "peers");
        entityStore.executeInTransaction(txn -> {
            entityStore.registerCustomPropertyType(txn, Peer.class, new Peer.PeerComparableBinding());
        });
        LOGGER.debug("Successfully initialized peers database.");
    }

    public void addPeer(Peer peer) throws IOException {
        entityStore.executeInTransaction(txn -> {
            Entity entity = txn.newEntity("peer");
            entity.setProperty("data", peer);
        });
    }

    public void close() throws IOException {
        entityStore.close();
        environment.close();
        LOGGER.debug("Closed peers database successfully");
    }

    public byte[] encodePeersList() {
        final byte[][] data = new byte[1][1];
        entityStore.executeInReadonlyTransaction(txn -> {
            data[0] = new byte[(int) (txn.getAll("peer").size() * Peer.PEER_DATA_SIZE)];
            final int[] offset = {0};
            txn.getAll("peer").forEach(entity -> {
                byte[] temp = ((Peer) entity.getProperty("data")).serialize();
                System.arraycopy(temp, 0, data[0], offset[0], Peer.PEER_DATA_SIZE);
                offset[0] += Peer.PEER_DATA_SIZE;
            });
        });
        return data[0];
    }

    /**
     * @see Collections#synchronizedSet(Set)
     */
    public Set<Peer> getPeers() {
        final Set<Peer> peers = new HashSet<>();
        entityStore.executeInReadonlyTransaction(txn -> {
            txn.getAll("peer").forEach(entity -> {
                peers.add((Peer) entity.getProperty("data"));
            });
        });
        return peers;
    }

}
