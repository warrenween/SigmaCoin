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

    private Random random = new Random(new Random().nextLong());
    private DataFactory dataFactory = new DataFactory();

    @Test
    public void testPutAndGet() throws IOException {
        BlockchainConfiguration config = new BlockchainConfiguration(
                "testPutAndGet.index", "testPutAndGet.dat", 3
        );
        BlockchainController controller = new BlockchainController(config);

        byte[] hash = new byte[3];
        random.nextBytes(hash);
        byte[] data = new byte[random.nextInt(20)];
        random.nextBytes(data);

        controller.put(hash, data);

        Assertions.assertThat(controller.get(hash)).containsExactly(data);
    }

    @Test
    public void testPutAndGetMultipleBlocks() throws IOException {
        BlockchainConfiguration config = new BlockchainConfiguration(
                "testPutAndGetMultiple.index", "testPutAndGetMultiple.dat", 3
        );
        BlockchainController controller = new BlockchainController(config);

        //First block
        byte[] hash1 = new byte[3];
        random.nextBytes(hash1);
        byte[] data1 = new byte[random.nextInt(20)];
        random.nextBytes(data1);
        controller.put(hash1, data1);

        //Second block
        byte[] hash2 = new byte[3];
        random.nextBytes(hash2);
        byte[] data2 = new byte[random.nextInt(20)];
        random.nextBytes(data2);
        controller.put(hash2, data2);

        //Third block
        byte[] hash3 = new byte[3];
        random.nextBytes(hash3);
        byte[] data3 = new byte[random.nextInt(20)];
        controller.put(hash3, data3);

        //Validating
        controller.get(hash1);
        Assertions.assertThat(controller.get(hash1)).containsExactly(data1);
        Assertions.assertThat(controller.get(hash2)).containsExactly(data2);
        Assertions.assertThat(controller.get(hash3)).containsExactly(data3);
    }

    @Test
    public void testDelete() throws IOException {
        BlockchainConfiguration config = new BlockchainConfiguration(
                "testDelete.index", "testDelete.dat", 3
        );
        BlockchainController controller = new BlockchainController(config);

        byte[] hash = new byte[3];
        random.nextBytes(hash);
        byte[] data = new byte[random.nextInt(20)];
        random.nextBytes(data);
        controller.put(hash, data);

        Assertions.assertThat(controller.delete(hash)).isEqualTo(true);

        Assertions.assertThat(controller.get(hash)).isEqualTo(null);
    }

    @Test
    public void testDeletingAndPutting() throws IOException {
        BlockchainConfiguration config = new BlockchainConfiguration(
                "testDeleteAndPut.index", "testDeleteAndPut.dat", 3
        );
        BlockchainController controller = new BlockchainController(config);

        byte[] hash1 = new byte[3];
        random.nextBytes(hash1);
        byte[] data = new byte[random.nextInt(20)];
        random.nextBytes(data);
        controller.put(hash1, data);

        Assertions.assertThat(controller.delete(hash1)).isEqualTo(true);
        Assertions.assertThat(controller.get(hash1)).isEqualTo(null);

        byte[] hash2 = new byte[3];
        random.nextBytes(hash1);
        byte[] data2 = new byte[random.nextInt(20)];
        controller.put(hash2, data2);

        Assertions.assertThat(controller.get(hash2)).containsExactly(data2);
    }

    @Test
    public void testPuttingDeletingAndGettingMultipleBlocks() throws IOException {
        BlockchainConfiguration config = new BlockchainConfiguration(
                "testPutDeleteAndGetMultiple.index", "testPutDeleteAndGetMultiple.dat", 3
        );
        BlockchainController controller = new BlockchainController(config);

        byte[] hash1 = new byte[3];
        random.nextBytes(hash1);
        byte[] data1 = new byte[random.nextInt(20)];
        random.nextBytes(data1);

        byte[] hash2 = new byte[3];
        random.nextBytes(hash2);
        byte[] data2 = new byte[random.nextInt(20)];
        random.nextBytes(data2);

        byte[] hash3 = new byte[3];
        random.nextBytes(hash3);
        byte[] data3 = new byte[random.nextInt(20)];
        random.nextBytes(data3);

        byte[] hash4 = new byte[3];
        random.nextBytes(hash4);
        byte[] data4 = new byte[random.nextInt(20)];
        random.nextBytes(data4);

        controller.put(hash1, data1);
        controller.put(hash2, data2);
        //controller.put(hash3, data3);
        controller.put(hash4, data4);

        Assertions.assertThat(controller.delete(hash2)).isEqualTo(true);

        Assertions.assertThat(controller.get(hash1)).containsExactly(data1);
        Assertions.assertThat(controller.get(hash2)).containsExactly();
        Assertions.assertThat(controller.get(hash3)).containsExactly();
        Assertions.assertThat(controller.get(hash4)).containsExactly(data4);

        Assertions.assertThat(controller.delete(hash2)).isEqualTo(false);

        controller.put(hash3, data3);

        Assertions.assertThat(controller.get(hash1)).containsExactly(data1);
        Assertions.assertThat(controller.get(hash2)).containsExactly();
        Assertions.assertThat(controller.get(hash3)).containsExactly(data3);
        Assertions.assertThat(controller.get(hash4)).containsExactly(data4);
    }

    @Test
    public void testReindex() throws IOException {
        BlockchainConfiguration config = new BlockchainConfiguration(
                "testReindex.index", "testReindex.dat", 3
        );
        BlockchainController controller = new BlockchainController(config);

        byte[] hash1 = new byte[3];
        random.nextBytes(hash1);
        byte[] data1 = new byte[random.nextInt(20)];
        random.nextBytes(data1);

        byte[] hash2 = new byte[3];
        random.nextBytes(hash2);
        byte[] data2 = new byte[random.nextInt(20)];
        random.nextBytes(data2);

        byte[] hash3 = new byte[3];
        random.nextBytes(hash3);
        byte[] data3 = new byte[random.nextInt(20)];
        random.nextBytes(data3);

        byte[] hash4 = new byte[3];
        random.nextBytes(hash4);
        byte[] data4 = new byte[random.nextInt(20)];
        random.nextBytes(data4);

        controller.put(hash1, data1);
        controller.put(hash2, data2);
        controller.put(hash3, data3);
        controller.put(hash4, data4);

        Assertions.assertThat(controller.delete(hash2)).isEqualTo(true);
        Assertions.assertThat(controller.delete(hash3)).isEqualTo(true);

        controller.reindex();

        Assertions.assertThat(controller.get(hash1)).isEqualTo(data1);
        Assertions.assertThat(controller.get(hash4)).isEqualTo(data4);

        if (new File(config.indexFile + ".old").exists()) {
            Fail.fail("Old index file must be deleted");
        }
        if (new File(config.blockchainFile + ".old").exists()) {
            Fail.fail("Old blockchain file must be deleted");
        }
    }

}
