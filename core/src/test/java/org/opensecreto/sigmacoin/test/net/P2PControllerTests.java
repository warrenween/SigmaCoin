package org.opensecreto.sigmacoin.test.net;

import org.opensecreto.sigmacoin.core.net.P2PController;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class P2PControllerTests {

    @Test
    public void testCreation() throws IOException {
        File dataFolder = new File("P2PController");
        dataFolder.mkdirs();

        P2PController controller = new P2PController(dataFolder);
    }

}
