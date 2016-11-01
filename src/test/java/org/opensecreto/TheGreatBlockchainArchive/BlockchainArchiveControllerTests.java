package org.opensecreto.TheGreatBlockchainArchive;

import javafx.util.Pair;
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
import java.util.ArrayList;
import java.util.Random;

public class BlockchainArchiveControllerTests {

    private Random random;
    private BlockchainArchiveConfiguration config;
    private DataFactory dataFactory;
    private ArrayList<Pair<byte[], String>> benchmarkData;

    private BlockchainArchiveController controllerForGetBenchmarks;

    @BeforeSuite
    public void prepare() {
        random = new Random();
        dataFactory = new DataFactory();

        benchmarkData = new ArrayList<>(500);
        for (int i = 0; i < 500; i++) {
            byte[] hash = new byte[256];
            random.nextBytes(hash);
            benchmarkData.add(new Pair<>(hash, dataFactory.getRandomChars(8, 2048)));
        }
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

    @Test(timeOut = 10 * 1000)
    public void benchmarkPut() throws IOException {
        config.setHashLength(256);
        BlockchainArchiveController controller = new BlockchainArchiveController(config);

        benchmarkData.forEach(data -> {
            try {
                controller.put(data.getKey(), data.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test(timeOut = 20 * 1000)
    public void benchmarkPutAndGetLinear() throws IOException {
        config.setHashLength(256);
        BlockchainArchiveController controller = new BlockchainArchiveController(config);

        benchmarkData.forEach(data -> {
            try {
                controller.put(data.getKey(), data.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        benchmarkData.forEach(data -> {
            try {
                Assertions.assertThat(controller.get(data.getKey())).isEqualTo(data.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test(timeOut = 20 * 1000)
    public void benchmarkPutAndGetRandom() throws IOException {
        config.setHashLength(256);
        BlockchainArchiveController controller = new BlockchainArchiveController(config);

        benchmarkData.forEach(data -> {
            try {
                controller.put(data.getKey(), data.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        for (int i = 0; i < 500; i++) {
            int index = random.nextInt(500);
            Assertions.assertThat(controller.get(benchmarkData.get(index).getKey()))
                    .isEqualTo(benchmarkData.get(index).getValue());
        }
    }

    @BeforeMethod
    public void prepareGetData(Method method) throws IOException {
        if (method.getName().equals("benchmarkGetLinear") || method.getName().equals("benchmarkGetRandom")) {
            config.setHashLength(256);
            controllerForGetBenchmarks = new BlockchainArchiveController(config);
            benchmarkData.forEach(data -> {
                try {
                    controllerForGetBenchmarks.put(data.getKey(), data.getValue());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Test(timeOut = 10 * 1000)
    public void benchmarkGetLinear() throws IOException {
        benchmarkData.forEach(data -> {
            try {
                Assertions.assertThat(controllerForGetBenchmarks.get(data.getKey())).isEqualTo(data.getValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test(timeOut = 10 * 1000)
    public void benchmarkGetRandom() throws IOException {
        for (int i = 0; i < 500; i++) {
            int index = random.nextInt(500);
            Assertions.assertThat(controllerForGetBenchmarks.get(benchmarkData.get(index).getKey()))
                    .isEqualTo(benchmarkData.get(index).getValue());
        }
    }

}
