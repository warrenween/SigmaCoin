package ru.opensecreto.sigmacoin.blockstorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.config.Constants;
import ru.opensecreto.sigmacoin.config.EnvironmentConfig;

public class BlockStorageConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockStorageConfigurator.class);

    public static BlockStorageConfiguration create() {
        LOGGER.info("Setting default START_SIZE {}.", EnvironmentConfig.BLOCK_STORAGE_DEFAULT_START_SIZE);
        int startSize = EnvironmentConfig.BLOCK_STORAGE_DEFAULT_START_SIZE;
        if (System.getProperty(Constants.BLOCK_STORAGE_START_SIZE_PROPERTY) != null) {
            try {
                startSize = Integer.parseUnsignedInt(System.getProperty(Constants.BLOCK_STORAGE_START_SIZE_PROPERTY));
            } catch (NumberFormatException e) {
                LOGGER.warn("Error parsing '{}' property.", Constants.BLOCK_STORAGE_START_SIZE_PROPERTY, e);
            }
        }

        LOGGER.info("Setting default ALLOCATE_START_SIZE {}.", EnvironmentConfig.BLOCK_STORAGE_DEFAULT_ALLOCATE_SIZE);
        int allocateSize = EnvironmentConfig.BLOCK_STORAGE_DEFAULT_ALLOCATE_SIZE;
        if (System.getProperty(Constants.BLOCK_STORAGE_ALLOCATE_SIZE_PROPERTY) != null) {
            try {
                allocateSize = Integer.parseUnsignedInt(System.getProperty(Constants.BLOCK_STORAGE_ALLOCATE_SIZE_PROPERTY));
            } catch (NumberFormatException e) {
                LOGGER.warn("Error parsing '{}' property.", Constants.BLOCK_STORAGE_ALLOCATE_SIZE_PROPERTY, e);
            }
        }

        return new BlockStorageConfiguration(startSize, allocateSize);
    }

}
