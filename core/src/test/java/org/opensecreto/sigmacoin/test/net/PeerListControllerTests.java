package org.opensecreto.sigmacoin.test.net;

import org.assertj.core.api.Assertions;
import org.opensecreto.sigmacoin.core.net.Peer;
import org.opensecreto.sigmacoin.core.net.PeersListController;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class PeerListControllerTests {

    private File file;

    @BeforeMethod
    public void prepare(Method method) {
        file = new File(method.getName());
        file.mkdirs();
    }

    @Test
    public void testAddingAndGetting() throws IOException {
        PeersListController controller = new PeersListController(file);

        Peer peer1 = PeerTest.generatePeer(true);
        Peer peer2 = PeerTest.generatePeer(true);
        Peer peer3 = PeerTest.generatePeer(true);
        Peer peer4 = PeerTest.generatePeer(true);
        Peer peer5 = PeerTest.generatePeer(true);

        controller.addPeer(peer1);
        controller.addPeer(peer2);
        controller.addPeer(peer3);
        controller.addPeer(peer4);
        controller.addPeer(peer5);

        Assertions.assertThat(controller.getPeer(0)).isEqualTo(peer1);
        Assertions.assertThat(controller.getPeer(1)).isEqualTo(peer2);
        Assertions.assertThat(controller.getPeer(2)).isEqualTo(peer3);
        Assertions.assertThat(controller.getPeer(3)).isEqualTo(peer4);
        Assertions.assertThat(controller.getPeer(4)).isEqualTo(peer5);

        Assertions.assertThat(controller.peerCount()).isEqualTo(5);
    }

}
