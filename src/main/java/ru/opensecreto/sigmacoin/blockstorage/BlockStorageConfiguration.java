package ru.opensecreto.sigmacoin.blockstorage;

import java.io.File;

public class BlockStorageConfiguration {

    public final File blockchainFoler;
    public final int hashLength;
    public final int blockMaxSize;
    public final int maxBlocksPerFile;

    public BlockStorageConfiguration(File blockchainFoler, int hashLength, int blockMaxSize, int maxBlocksPerFile) {
        this.blockchainFoler = blockchainFoler;
        this.hashLength = hashLength;
        this.blockMaxSize = blockMaxSize;
        this.maxBlocksPerFile = maxBlocksPerFile;
    }
}
