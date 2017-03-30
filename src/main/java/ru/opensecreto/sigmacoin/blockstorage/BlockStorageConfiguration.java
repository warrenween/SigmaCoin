package ru.opensecreto.sigmacoin.blockstorage;

import java.io.File;

public class BlockStorageConfiguration {

    public final File blockchainFolder;
    public final int hashLength;
    public final int blockMaxSize;
    public final int maxBlocksPerFile;

    public BlockStorageConfiguration(File blockchainFolder, int hashLength, int blockMaxSize, int maxBlocksPerFile) {
        this.blockchainFolder = blockchainFolder;
        this.hashLength = hashLength;
        this.blockMaxSize = blockMaxSize;
        this.maxBlocksPerFile = maxBlocksPerFile;
    }
}
