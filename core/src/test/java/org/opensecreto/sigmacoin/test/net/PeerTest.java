package org.opensecreto.sigmacoin.test.net;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmacoin.core.net.Peer;
import org.testng.annotations.Test;

import java.net.UnknownHostException;

public class PeerTest {


    @Test
    public void testPort() throws UnknownHostException {
        Peer peer = new Peer(new byte[4], 128, 512);
        Assertions.assertThat(peer.getPort()).isEqualTo(128);
    }

    @Test
    public void testLastSeen() throws UnknownHostException {
        Peer peer = new Peer(new byte[4], 128, 512);
        Assertions.assertThat(peer.getLastSeen()).isEqualTo(512);
    }

    @Test
    public void testUpdateLastSeen() throws UnknownHostException {
        Peer peer = new Peer(new byte[4], 128, 512);
        peer.updateLastSeen(2048);
        Assertions.assertThat(peer.getLastSeen()).isEqualTo(2048);
    }

    @Test
    public void testUpdateLastSeenWithBadTime() throws UnknownHostException {
        Peer peer = new Peer(new byte[4], 128, 512);
        peer.updateLastSeen(64);
        Assertions.assertThat(peer.getLastSeen()).isEqualTo(512);
    }

}
