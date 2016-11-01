package org.opensecreto.TheGreatBlockchainArchive;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.fluttercode.datafactory.impl.DataFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Random;

public class BlockchainArchiveControllerTests {

    private Random random;
    private BlockchainArchiveConfiguration config;
    private DataFactory dataFactory;

    @BeforeSuite
    public void prepare() {
        random = new Random();
        dataFactory = new DataFactory();
    }

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

    @Test
    public void testPutAndGet() throws IOException {
        config.setHashLength(4);
        BlockchainArchiveController controller = new BlockchainArchiveController(config);

        byte[] hash = new byte[4];
        random.nextBytes(hash);
        String data = dataFactory.getRandomChars(5, 20);

        controller.put(hash, data);

        String resultData = controller.get(hash);
        Assertions.assertThat(resultData).isEqualTo(data);
    }

    @Test
    public void testPutAndGetStringHash() throws IOException {
        config.setHashLength(4);
        BlockchainArchiveController controller = new BlockchainArchiveController(config);

        byte[] hash = new byte[4];
        random.nextBytes(hash);
        String data = dataFactory.getRandomChars(5, 20);

        controller.put(DatatypeConverter.printHexBinary(hash), data);

        String resultData = controller.get(DatatypeConverter.printHexBinary(hash));
        Assertions.assertThat(resultData).isEqualTo(data);
    }

    @Test
    public void testPutAndGetMultipleBlocks() throws IOException {
        config.setHashLength(4);
        BlockchainArchiveController controller = new BlockchainArchiveController(config);

        //First block
        byte[] hash1 = new byte[4];
        random.nextBytes(hash1);
        String data1 = dataFactory.getRandomChars(5, 20);
        controller.put(hash1, data1);

        //Second block
        byte[] hash2 = new byte[4];
        random.nextBytes(hash2);
        String data2 = dataFactory.getRandomChars(5, 20);
        controller.put(hash2, data2);

        //Third block
        byte[] hash3 = new byte[4];
        random.nextBytes(hash3);
        String data3 = dataFactory.getRandomChars(5, 20);
        controller.put(hash3, data3);

        //Validating
        String resultData1 = controller.get(hash1);
        Assertions.assertThat(resultData1).isEqualTo(data1);
        String resultData2 = controller.get(hash2);
        Assertions.assertThat(resultData2).isEqualTo(data2);
        String resultData3 = controller.get(hash3);
        Assertions.assertThat(resultData3).isEqualTo(data3);
    }

}
