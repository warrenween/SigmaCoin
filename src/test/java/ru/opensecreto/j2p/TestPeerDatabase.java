package ru.opensecreto.j2p;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class TestPeerDatabase {

    @Test
    public void testAddingPeers() throws IOException {
        File dbFile = new File("testAddingPeers.db");
        PeerDatabase db = new PeerDatabase(dbFile);

        Peer peer1 = new Peer(InetAddress.getByName("127.0.0.1"), 1234, 128, 256);
        Peer peer2 = new Peer(InetAddress.getByName("1080::8:800:200C:417A"), 1234, 128, 256);

        db.addPeer(peer1);
        db.addPeer(peer2);

        Assertions.assertThat(db.getPeerCount()).isEqualTo(2);

        Peer peerLoaded1 = db.getPeer(0);
        Peer peerLoaded2 = db.getPeer(1);

        Assertions.assertThat(peerLoaded1).isEqualTo(peer1);
        Assertions.assertThat(peerLoaded2).isEqualTo(peer2);
    }

}
