package ru.opensecreto.sigmacoin.blockchain;

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

public class BlockchainControllerTests {

    private Random random;
    private BlockchainConfiguration config;
    private DataFactory dataFactory;

    @BeforeSuite
    public void prepare() {
        random = new Random();
        dataFactory = new DataFactory();
    }

    @Test
    public void testStringWrapperMethods() throws IOException {
        config.setHashLength(3);
        BlockchainController controller = new BlockchainController(config);

        byte[] hash = new byte[3];
        random.nextBytes(hash);
        String data = dataFactory.getRandomChars(10, 20);

        controller.put(DatatypeConverter.printHexBinary(hash), data);
        Assertions.assertThat(controller.get(hash)).isEqualTo(data);
        Assertions.assertThat(controller.delete(hash)).isEqualTo(true);
        Assertions.assertThat(controller.get(hash)).isEqualTo(null);
        Assertions.assertThat(controller.delete(hash)).isEqualTo(false);
    }

    @BeforeMethod
    public void prepareConfig(Method method) {
        config = new BlockchainConfiguration();
        config.setIndexFile(method.getName() + "-index.dat");
        config.setBlockchainFile(method.getName() + "-blockchain.dat");
    }

    @Test
    public void testCreation() throws IOException {
        BlockchainController controller = new BlockchainController(config);
        if (!new File(config.getIndexFile()).exists()) {
            Fail.fail("Index file must exist");
        }
        if (!new File(config.getBlockchainFile()).exists()) {
            Fail.fail("Blockchain file must exist");
        }
    }

    @Test
    public void testPutAndGet() throws IOException {
        config.setHashLength(3);
        BlockchainController controller = new BlockchainController(config);

        byte[] hash = new byte[3];
        random.nextBytes(hash);
        String data = dataFactory.getRandomChars(5, 20);

        controller.put(hash, data);

        String resultData = controller.get(hash);
        Assertions.assertThat(resultData).isEqualTo(data);
    }

    @Test
    public void testPutAndGetMultipleBlocks() throws IOException {
        config.setHashLength(3);
        BlockchainController controller = new BlockchainController(config);

        //First block
        byte[] hash1 = new byte[3];
        random.nextBytes(hash1);
        String data1 = dataFactory.getRandomChars(5, 20);
        controller.put(hash1, data1);

        //Second block
        byte[] hash2 = new byte[3];
        random.nextBytes(hash2);
        String data2 = dataFactory.getRandomChars(5, 20);
        controller.put(hash2, data2);

        //Third block
        byte[] hash3 = new byte[3];
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

    @Test
    public void testDelete() throws IOException {
        config.setHashLength(3);
        BlockchainController controller = new BlockchainController(config);

        byte[] hash = new byte[3];
        random.nextBytes(hash);
        String data = dataFactory.getRandomChars(10, 20);
        controller.put(hash, data);

        Assertions.assertThat(controller.delete(hash)).isEqualTo(true);

        Assertions.assertThat(controller.get(hash)).isEqualTo(null);
    }

    @Test
    public void testDeletingAndPutting() throws IOException {
        config.setHashLength(3);
        BlockchainController controller = new BlockchainController(config);

        byte[] hash1 = new byte[3];
        random.nextBytes(hash1);
        String data = dataFactory.getRandomChars(10, 20);
        controller.put(hash1, data);

        Assertions.assertThat(controller.delete(hash1)).isEqualTo(true);
        Assertions.assertThat(controller.get(hash1)).isEqualTo(null);

        byte[] hash2 = new byte[3];
        random.nextBytes(hash1);
        String data2 = dataFactory.getRandomChars(10, 20);
        controller.put(hash2, data2);

        Assertions.assertThat(controller.get(hash2)).isEqualTo(data2);
    }

    @Test
    public void testPuttingDeletingAndGettingMultipleBlocks() throws IOException {
        config.setHashLength(3);
        BlockchainController controller = new BlockchainController(config);

        byte[] hash1 = new byte[3];
        random.nextBytes(hash1);
        String data1 = dataFactory.getRandomChars(10, 20);

        byte[] hash2 = new byte[3];
        random.nextBytes(hash2);
        String data2 = dataFactory.getRandomChars(10, 20);

        byte[] hash3 = new byte[3];
        random.nextBytes(hash3);
        String data3 = dataFactory.getRandomChars(10, 20);

        byte[] hash4 = new byte[3];
        random.nextBytes(hash4);
        String data4 = dataFactory.getRandomChars(10, 20);

        controller.put(hash1, data1);
        controller.put(hash2, data2);
        //controller.put(hash3, data3);
        controller.put(hash4, data4);

        Assertions.assertThat(controller.delete(hash2)).isEqualTo(true);

        Assertions.assertThat(controller.get(hash1)).isEqualTo(data1);
        Assertions.assertThat(controller.get(hash2)).isEqualTo(null);
        Assertions.assertThat(controller.get(hash3)).isEqualTo(null);
        Assertions.assertThat(controller.get(hash4)).isEqualTo(data4);

        Assertions.assertThat(controller.delete(hash2)).isEqualTo(false);

        controller.put(hash3, data3);

        Assertions.assertThat(controller.get(hash1)).isEqualTo(data1);
        Assertions.assertThat(controller.get(hash2)).isEqualTo(null);
        Assertions.assertThat(controller.get(hash3)).isEqualTo(data3);
        Assertions.assertThat(controller.get(hash4)).isEqualTo(data4);
    }

    @Test
    public void testReindex() throws IOException {
        config.setHashLength(3);
        BlockchainController controller = new BlockchainController(config);

        byte[] hash1 = new byte[3];
        random.nextBytes(hash1);
        String data1 = dataFactory.getRandomChars(10, 20);

        byte[] hash2 = new byte[3];
        random.nextBytes(hash2);
        String data2 = dataFactory.getRandomChars(10, 20);

        byte[] hash3 = new byte[3];
        random.nextBytes(hash3);
        String data3 = dataFactory.getRandomChars(10, 20);

        byte[] hash4 = new byte[3];
        random.nextBytes(hash4);
        String data4 = dataFactory.getRandomChars(10, 20);

        controller.put(hash1, data1);
        controller.put(hash2, data2);
        controller.put(hash3, data3);
        controller.put(hash4, data4);

        Assertions.assertThat(controller.delete(hash2)).isEqualTo(true);
        Assertions.assertThat(controller.delete(hash3)).isEqualTo(true);

        controller.reindex();

        Assertions.assertThat(controller.get(hash1)).isEqualTo(data1);
        Assertions.assertThat(controller.get(hash4)).isEqualTo(data4);

        if (new File(config.getIndexFile() + ".old").exists()) {
            Fail.fail("Old index file must be deleted");
        }
        if (new File(config.getBlockchainFile() + ".old").exists()) {
            Fail.fail("Old blockchain file must be deleted");
        }
    }

}
