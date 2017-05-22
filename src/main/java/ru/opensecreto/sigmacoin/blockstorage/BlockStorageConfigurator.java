package ru.opensecreto.sigmacoin.blockstorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockStorageConfigurator {

    /**
     * 128 mib
     */
    public static final int DEFAULT_START_SIZE = 134217728;
    public static final int DEFAULT_ALLOCATE_SIZE = 67108864;
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockStorageConfigurator.class);

    public static BlockStorageConfiguration create() {
        LOGGER.info("Setting default START_SIZE {}.", DEFAULT_START_SIZE);
        int startSize = DEFAULT_START_SIZE;
        if (System.getProperty("blockstorage.startsize") != null) {
            try {
                startSize = Integer.parseUnsignedInt(System.getProperty("blockstorage.startsize"));
            } catch (NumberFormatException e) {
                LOGGER.warn("Error parsing 'blockstorage.startsize' property.", e);
            }
        }

        LOGGER.info("Setting default ALLOCATE_START_SIZE {}.", DEFAULT_ALLOCATE_SIZE);
        int allocateSize = DEFAULT_ALLOCATE_SIZE;
        if (System.getProperty("blockstorage.allocatesize") != null) {
            try {
                allocateSize = Integer.parseUnsignedInt(System.getProperty("blockstorage.allocatesize"));
            } catch (NumberFormatException e) {
                LOGGER.warn("Error parsing 'blockstorage.allocatesize' property.", e);
            }
        }

        return new BlockStorageConfiguration(startSize, allocateSize);
    }

}
