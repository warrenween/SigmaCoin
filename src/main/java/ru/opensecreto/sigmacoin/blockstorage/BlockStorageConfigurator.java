package ru.opensecreto.sigmacoin.blockstorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.config.EnvironmentConfig;

public class BlockStorageConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockStorageConfigurator.class);

    public static BlockStorageConfiguration create() {
        LOGGER.info("Setting default START_SIZE {}.", EnvironmentConfig.BLOCK_STORAGE_DEFAULT_START_SIZE);
        int startSize = EnvironmentConfig.BLOCK_STORAGE_DEFAULT_START_SIZE;
        if (System.getProperty("blockstorage.startsize") != null) {
            try {
                startSize = Integer.parseUnsignedInt(System.getProperty("blockstorage.startsize"));
            } catch (NumberFormatException e) {
                LOGGER.warn("Error parsing 'blockstorage.startsize' property.", e);
            }
        }

        LOGGER.info("Setting default ALLOCATE_START_SIZE {}.", EnvironmentConfig.BLOCK_STORAGE_DEFAULT_ALLOCATE_SIZE);
        int allocateSize = EnvironmentConfig.BLOCK_STORAGE_DEFAULT_ALLOCATE_SIZE;
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
