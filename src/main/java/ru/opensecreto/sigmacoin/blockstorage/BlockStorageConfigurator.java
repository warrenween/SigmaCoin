package ru.opensecreto.sigmacoin.blockstorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockStorageConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockStorageConfigurator.class);

    /**
     * 128 mib
     */
    public static final int DEFAULT_START_SIZE = 134217728;
    public static final int START_SIZE;

    public static final int DEFAULT_ALLOCATE_SIZE = 67108864;
    public static final int ALLOCATE_SIZE;

    static {
        int START_SIZE_TEMP;
        if (System.getProperty("blockstorage.startsize") == null) {
            LOGGER.info("'blockstorage.startsize' property is not defined. Setting to default {}.", DEFAULT_START_SIZE);
            START_SIZE_TEMP = DEFAULT_START_SIZE;
        } else {
            try {
                START_SIZE_TEMP = Integer.parseUnsignedInt(System.getProperty("blockstorage.startsize"));
            } catch (NumberFormatException e) {
                LOGGER.warn("Error parsing 'blockstorage.startsize' property. " +
                        "Setting to default {}.", DEFAULT_START_SIZE, e);
                START_SIZE_TEMP = DEFAULT_START_SIZE;
            }
        }
        START_SIZE = START_SIZE_TEMP;

        int ALLOCATE_SIZE_TEMP;
        if (System.getProperty("blockstorage.allocatesize") == null) {
            LOGGER.info("'blockstorage.allocatesize' property is not defined. Setting to default {}.", DEFAULT_ALLOCATE_SIZE);
            ALLOCATE_SIZE_TEMP = DEFAULT_ALLOCATE_SIZE;
        } else {
            try {
                ALLOCATE_SIZE_TEMP = Integer.parseUnsignedInt(System.getProperty("blockstorage.allocatesize"));
            } catch (NumberFormatException e) {
                LOGGER.warn("Error parsing 'blockstorage.allocatesize' property. " +
                        "Setting to default {}.", DEFAULT_ALLOCATE_SIZE, e);
                ALLOCATE_SIZE_TEMP = DEFAULT_ALLOCATE_SIZE;
            }
        }
        ALLOCATE_SIZE = ALLOCATE_SIZE_TEMP;
    }

}
