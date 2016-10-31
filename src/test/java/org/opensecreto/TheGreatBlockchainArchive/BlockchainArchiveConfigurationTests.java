package org.opensecreto.TheGreatBlockchainArchive;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.fluttercode.datafactory.impl.DataFactory;
import org.opensecreto.TheGreatBlockchainArchive.exceptions.ImmutableFieldException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BlockchainArchiveConfigurationTests {

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
        BlockchainArchiveConfiguration config = new BlockchainArchiveConfiguration();

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
        BlockchainArchiveConfiguration config = new BlockchainArchiveConfiguration();

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
        BlockchainArchiveConfiguration config = new BlockchainArchiveConfiguration();

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
        BlockchainArchiveConfiguration config = new BlockchainArchiveConfiguration();

        Assertions.assertThat(config.isImmutable()).isEqualTo(false);
        config.setImmutable();
        Assertions.assertThat(config.isImmutable()).isEqualTo(true);
    }

}
