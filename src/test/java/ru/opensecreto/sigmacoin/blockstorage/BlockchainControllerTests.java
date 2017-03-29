package ru.opensecreto.sigmacoin.blockstorage;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.testng.annotations.Test;
import ru.opensecreto.TestUtils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class BlockchainControllerTests {

    private Random random = new Random(new Random().nextLong());

    @Test
    public void testPutAndGet() throws IOException {
        BlockchainConfiguration config = new BlockchainConfiguration(
                "testPutAndGet.index", "testPutAndGet.dat", 3
        );
        BlockchainController controller = new BlockchainController(config);

        byte[] hash = TestUtils.getFixedArray(3);
        byte[] data = TestUtils.getRandomArray(20);

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
        byte[] hash1 = TestUtils.getFixedArray(3);
        byte[] data1 = TestUtils.getRandomArray(20);
        controller.put(hash1, data1);

        //Second block
        byte[] hash2 = TestUtils.getFixedArray(3);
        byte[] data2 = TestUtils.getRandomArray(20);
        controller.put(hash2, data2);

        //Third block
        byte[] hash3 = TestUtils.getFixedArray(3);
        byte[] data3 = TestUtils.getRandomArray(20);
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

        byte[] hash = TestUtils.getFixedArray(3);
        byte[] data = TestUtils.getRandomArray(20);
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

        byte[] hash1 = TestUtils.getFixedArray(3);
        byte[] data = TestUtils.getRandomArray(20);
        controller.put(hash1, data);

        Assertions.assertThat(controller.delete(hash1)).isEqualTo(true);
        Assertions.assertThat(controller.get(hash1)).isEqualTo(null);

        byte[] hash2 = TestUtils.getFixedArray(3);
        byte[] data2 = TestUtils.getRandomArray(20);

        Assertions.assertThat(controller.get(hash2)).containsExactly(data2);
    }

    @Test
    public void testPuttingDeletingAndGettingMultipleBlocks() throws IOException {
        BlockchainConfiguration config = new BlockchainConfiguration(
                "testPutDeleteAndGetMultiple.index", "testPutDeleteAndGetMultiple.dat", 3
        );
        BlockchainController controller = new BlockchainController(config);

        byte[] hash1 = TestUtils.getFixedArray(3);
        byte[] data1 = TestUtils.getRandomArray(20);

        byte[] hash2 = TestUtils.getFixedArray(3);
        byte[] data2 = TestUtils.getRandomArray(20);

        byte[] hash3 = TestUtils.getFixedArray(3);
        byte[] data3 = TestUtils.getRandomArray(20);

        byte[] hash4 = TestUtils.getFixedArray(3);
        byte[] data4 = TestUtils.getRandomArray(20);

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

        byte[] hash1 = TestUtils.getFixedArray(3);
        byte[] data1 = TestUtils.getRandomArray(20);

        byte[] hash2 = TestUtils.getFixedArray(3);
        byte[] data2 = TestUtils.getRandomArray(20);

        byte[] hash3 = TestUtils.getFixedArray(3);
        byte[] data3 = TestUtils.getRandomArray(20);

        byte[] hash4 = TestUtils.getFixedArray(3);
        byte[] data4 = TestUtils.getRandomArray(20);

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
            Fail.fail("Old blockstorage file must be deleted");
        }
    }

}
