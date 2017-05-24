package ru.opensecreto.sigmacoin.config;

public class EnvironmentConfig {

    /**
     * Default size of block storage database upon creation. 128MiB.
     */
    public static final int BLOCK_STORAGE_DEFAULT_START_SIZE = 134217728;
    /**
     * How much increase block storage database when it is full. 64 MiB
     */
    public static final int BLOCK_STORAGE_DEFAULT_ALLOCATE_SIZE = 67108864;
}
