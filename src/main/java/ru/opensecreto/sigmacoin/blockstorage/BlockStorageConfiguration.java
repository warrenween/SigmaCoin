package ru.opensecreto.sigmacoin.blockstorage;

public class BlockStorageConfiguration {

    public final int storageStartSize;
    public final int allocateSize;

    public BlockStorageConfiguration(int storageStartSize, int allocateSize) {
        if (storageStartSize < 0) throw new IllegalArgumentException("storageStartSize must be positive");
        if (allocateSize < 1) throw new IllegalArgumentException("allocateSize must be >= 1");

        this.storageStartSize = storageStartSize;
        this.allocateSize = allocateSize;
    }
}
