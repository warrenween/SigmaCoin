package ru.opensecreto.sigmacoin.blockchain;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.fluttercode.datafactory.impl.DataFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.blockchain.exceptions.ImmutableFieldException;

public class BlockchainConfigurationTests {

    private String randomStringA;
    private String randomStringB;
    private int randomIntA;
    private int randomIntB;

    @BeforeTest
    public void prepareTestData() {
        DataFactory dataFactory = new DataFactory();
        randomStringA = dataFactory.getRandomChars(6, 18);
        randomStringB = dataFactory.getRandomChars(6, 18);
        randomIntA = dataFactory.getNumberBetween(0, Integer.MAX_VALUE);
        randomIntB = dataFactory.getNumberBetween(0, Integer.MAX_VALUE);
    }

    @Test
    public void testIndexFile() {
        BlockchainConfiguration config = new BlockchainConfiguration();

        try {
            config.setIndexFile(randomStringA);
            Assertions.assertThat(config.getIndexFile()).isEqualTo(randomStringA);
        } catch (ImmutableFieldException e) {
            Fail.fail("No exception is expected", e);
        }
        config.setImmutable();

        Assertions.assertThatThrownBy(() -> config.setIndexFile(randomStringB)).isInstanceOf(ImmutableFieldException.class);
        Assertions.assertThat(config.getIndexFile()).isEqualTo(randomStringA);
    }

    @Test
    public void testBlockchainFile() {
        BlockchainConfiguration config = new BlockchainConfiguration();

        try {
            config.setBlockchainFile(randomStringA);
            Assertions.assertThat(config.getBlockchainFile()).isEqualTo(randomStringA);
        } catch (ImmutableFieldException e) {
            Fail.fail("No exception is expected", e);
        }
        config.setImmutable();

        Assertions.assertThatThrownBy(() -> config.setBlockchainFile(randomStringB)).isInstanceOf(ImmutableFieldException.class);
        Assertions.assertThat(config.getBlockchainFile()).isEqualTo(randomStringA);
    }

    @Test
    public void testHashLength() {
        BlockchainConfiguration config = new BlockchainConfiguration();

        try {
            config.setHashLength(randomIntA);
            Assertions.assertThat(config.getHashLength()).isEqualTo(randomIntA);
        } catch (ImmutableFieldException e) {
            Fail.fail("No exception is expected", e);
        }
        config.setImmutable();

        Assertions.assertThatThrownBy(() -> config.setHashLength(randomIntB)).isInstanceOf(ImmutableFieldException.class);
        Assertions.assertThat(config.getHashLength()).isEqualTo(randomIntA);
    }

    @Test
    public void testImmutable() {
        BlockchainConfiguration config = new BlockchainConfiguration();

        Assertions.assertThat(config.isImmutable()).isEqualTo(false);
        config.setImmutable();
        Assertions.assertThat(config.isImmutable()).isEqualTo(true);
    }

}
