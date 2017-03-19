package ru.opensecreto.sigmacoin.coretest.net;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import ru.opensecreto.sigmacoin.core.net.Peer;
import ru.opensecreto.sigmacoin.core.net.PeersListController;
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

    @Test
    public void testForEach() throws IOException {
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

        final int[] i = new int[]{0};
        controller.forEach(peer -> {
            switch (i[0]) {
                case 0:
                    Assertions.assertThat(peer).isEqualTo(peer1);
                    break;
                case 1:
                    Assertions.assertThat(peer).isEqualTo(peer2);
                    break;
                case 2:
                    Assertions.assertThat(peer).isEqualTo(peer3);
                    break;
                case 3:
                    Assertions.assertThat(peer).isEqualTo(peer4);
                    break;
                case 4:
                    Assertions.assertThat(peer).isEqualTo(peer5);
                    break;
                default:
                    Fail.fail("ForEach must execute only 5 times");
            }
            i[0]++;
        });
    }

}
