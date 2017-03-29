package ru.opensecreto.sigmacoin.blockchain;

import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeTest;

import java.util.Random;

public class BlockchainConfigurationTests {


    @BeforeTest
    public void testFields() {
        String index = "abc";
        String blockchain = "cba";
        int hash = new Random().nextInt();

        BlockchainConfiguration config = new BlockchainConfiguration(index, blockchain, hash);

        Assertions.assertThat(config.indexFile).isEqualTo(index);
        Assertions.assertThat(config.blockchainFile).isEqualTo(blockchain);
        Assertions.assertThat(config.hashLength).isEqualTo(hash);
    }

}
