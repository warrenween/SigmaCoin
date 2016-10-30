package org.opensecreto.thegreatarchive;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Fail;
import org.fluttercode.datafactory.impl.DataFactory;
import org.opensecreto.TheGreatBlockchainArchive.DatabaseConfiguration;
import org.opensecreto.TheGreatBlockchainArchive.exceptions.ImmutableFieldException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class DatabaseConfigurationTests {

    private String randomStringA;
    private String randomStringB;

    @BeforeTest
    public void prepareTestData() {
        DataFactory dataFactory = new DataFactory();
        randomStringA = dataFactory.getRandomChars(6, 18);
        randomStringB = dataFactory.getRandomChars(6, 18);
    }

    @Test
    public void testPath() {
        DatabaseConfiguration config = new DatabaseConfiguration();

        try {
            config.setPath(randomStringA);
            Assertions.assertThat(config.getPath()).isEqualTo(randomStringA);
        } catch (ImmutableFieldException e) {
            Fail.fail("No exception is expected", e);
        }
        config.setImmutable();

        Assertions.assertThatThrownBy(() -> config.setPath(randomStringB)).isInstanceOf(ImmutableFieldException.class);
        Assertions.assertThat(config.getPath()).isEqualTo(randomStringA);
    }

    @Test
    public void testImmutable() {
        DatabaseConfiguration config = new DatabaseConfiguration();

        Assertions.assertThat(config.isImmutable()).isEqualTo(false);
        config.setImmutable();
        Assertions.assertThat(config.isImmutable()).isEqualTo(true);
    }

}
