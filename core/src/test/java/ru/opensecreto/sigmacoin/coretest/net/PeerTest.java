package ru.opensecreto.sigmacoin.coretest.net;

import org.assertj.core.api.Assertions;
import ru.opensecreto.sigmacoin.core.net.Peer;
import org.testng.annotations.Test;

import java.net.UnknownHostException;
import java.util.Random;

public class PeerTest {

    private static Random random = new Random(0);

    public static Peer generatePeer(boolean ipv4) throws UnknownHostException {
        random.setSeed(System.nanoTime());
        byte[] address;
        if (ipv4) {
            address = new byte[4];
        } else {
            address = new byte[16];
        }
        random.nextBytes(address);
        int port = random.nextInt();
        long time = random.nextLong();
        return new Peer(address, port, time);
    }

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

    @Test
    public void testConstructionAndDeconstructionIpv4() throws UnknownHostException {
        Peer scrPeer = new Peer(new byte[]{21, 40, 120, 31}, 45896, 1254582L);

        Peer constructedPeer = Peer.constructFromBytes(Peer.deconstructIntoBytes(scrPeer));

        Assertions.assertThat(constructedPeer.getAddress().getAddress()).containsOnly(scrPeer.getAddress().getAddress());
        Assertions.assertThat(constructedPeer.getPort()).isEqualTo(45896);
        Assertions.assertThat(constructedPeer.getLastSeen()).isEqualTo(1254582L);
    }

    @Test
    public void testConstructionAndDeconstructionIpv6() throws UnknownHostException {
        Peer scrPeer = new Peer(new byte[]{21, 40, 120, 31, 52, 48, 32, 89,
                33, 44, 120, 0, 48, 75, 99, 105}, 478565, 9415631156L);

        Peer constructedPeer = Peer.constructFromBytes(Peer.deconstructIntoBytes(scrPeer));

        Assertions.assertThat(constructedPeer.getAddress().getAddress()).containsOnly(scrPeer.getAddress().getAddress());
        Assertions.assertThat(constructedPeer.getPort()).isEqualTo(478565);
        Assertions.assertThat(constructedPeer.getLastSeen()).isEqualTo(9415631156L);
    }

    @Test
    public void testEqualsA() throws UnknownHostException {
        Assertions.assertThat(generatePeer(true).equals(null)).isEqualTo(false);

        Assertions.assertThat(generatePeer(false).equals(generatePeer(true)));
        Assertions.assertThat(generatePeer(true).equals(generatePeer(false)));

        Assertions.assertThat(
                new Peer(new byte[]{18, 19, 20, 21}, 123456, 123456789)
                        .equals(new Peer(new byte[]{18, 19, 20, 21}, 123456, 123456789))
        ).isEqualTo(true);

        Assertions.assertThat(
                new Peer(new byte[]{1, 2, 3, 4}, 123456, 123456789)
                        .equals(new Peer(new byte[]{0, 1, 2, 3}, 123456, 123456789))
        ).isEqualTo(false);

        Assertions.assertThat(
                new Peer(new byte[]{5, 6, 7, 8}, 123, 123456789)
                        .equals(new Peer(new byte[]{5, 6, 7, 8}, 123456, 123456789))
        ).isEqualTo(false);

        Assertions.assertThat(
                new Peer(new byte[]{55, 66, 77, 88}, 987, 123)
                        .equals(new Peer(new byte[]{55, 66, 77, 88}, 987, 103))
        ).isEqualTo(false);
    }

}
