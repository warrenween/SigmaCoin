package ru.opensecreto.sigmacoin.blockchain;

public class BlockchainConfiguration {

    public final String indexFile;
    public final String blockchainFile;
    public final int hashLength;

    public BlockchainConfiguration(String indexFile, String blockchainFile, int hashLength) {
        this.indexFile = indexFile;
        this.blockchainFile = blockchainFile;
        this.hashLength = hashLength;
    }

}
