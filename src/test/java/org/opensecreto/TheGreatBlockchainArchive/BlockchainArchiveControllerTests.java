package org.opensecreto.TheGreatBlockchainArchive;

import org.assertj.core.api.Fail;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class BlockchainArchiveControllerTests {

    private BlockchainArchiveConfiguration config;

    @BeforeMethod
    public void prepareConfig(Method method) {
        config = new BlockchainArchiveConfiguration();
        config.setIndexFile(method.getName() + "-index.dat");
        config.setBlockchainFile(method.getName() + "-blockchain.dat");
    }

    @Test
    public void testCreation() throws IOException {
        BlockchainArchiveController controller = new BlockchainArchiveController(config);
        if (!new File(config.getIndexFile()).exists()) {
            Fail.fail("Index file must exist");
        }
        if (!new File(config.getBlockchainFile()).exists()) {
            Fail.fail("Blockchain file must exist");
        }
    }

}
