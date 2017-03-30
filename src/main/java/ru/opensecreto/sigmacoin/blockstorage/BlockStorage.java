package ru.opensecreto.sigmacoin.blockstorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.blockstorage.exception.BlockNotFoundException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Class for storing blocks.
 * Database consists of index file and block files.
 * Blocks are stored in different block files.
 * Index file points hash to file.
 * Block files contains block data and index for blocks offset and length.
 */
public class BlockStorage {

    public static final Logger LOGGER = LoggerFactory.getLogger(BlockStorage.class);

    public BlockStorageConfiguration config;

    private File indexFile;
    private RandomAccessFile index;

    public BlockStorage(BlockStorageConfiguration configuration) throws IOException {
        if (configuration == null) {
            LOGGER.error("Configuration is {}.", configuration);
        }
        LOGGER.info("Starting initialization.");
        this.config = configuration;

        if (!config.blockchainFolder.exists()) {
            LOGGER.debug("Blockstorage folder {} does not exists. Creating.", config.blockchainFolder);
            configuration.blockchainFolder.mkdirs();
        }

        indexFile = new File(config.blockchainFolder, "index.dat");
        index = new RandomAccessFile(indexFile, "rws");

    }

    /**
     * Get block.
     *
     * @param hash hash of given block
     * @return block with given hash
     * @throws IOException              if I/O exception happens
     * @throws IllegalArgumentException if hash has incorrect length
     * @throws BlockNotFoundException   if block with given hash was not found
     */
    public byte[] getBlock(byte[] hash) throws IOException, IllegalArgumentException, BlockNotFoundException {
        return null;
    }

    /**
     * Check if blockstorage contains block with given hash.
     *
     * @param hash hash of block to be checked
     * @return true if block was found, false otherwise
     * @throws IllegalArgumentException if hash has incorrect length
     * @throws IOException              if I/O exception happens
     */
    public boolean hasBlock(byte[] hash) throws IllegalArgumentException, IOException {
        verifyHash(hash);
        return findBlock(hash) >= 0;
    }

    /**
     * Removes block with given hash.
     *
     * @param hash hash of block to be removed
     * @return true if block was removed, false blockstorage does not contain block with given hash
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public boolean deleteBlock(byte[] hash) throws IOException, IllegalArgumentException {
        verifyHash(hash);
        if (!hasBlock(hash)) return false;

        return false;
    }

    /**
     * Put block to this block storage.
     *
     * @param hash hash of given block
     * @param data block data
     * @return true if block was successfully saved, false if block already exists
     * @throws IOException              if I/O error happens
     * @throws IllegalArgumentException if hash or block has incorrect length
     */
    public boolean putBlock(byte[] hash, byte[] data) throws IOException, IllegalArgumentException {
        verifyHash(hash);
        if (data.length > config.blockMaxSize) throw new IllegalArgumentException("Block is too big.");
        return false;
    }

    /**
     * Get id of file, where this block is stored.
     *
     * @param hash hash if block to be found
     * @return id of file, where this block is stored, or -1 if not found
     * @throws IllegalArgumentException if hash has incorrect length
     * @throws IOException              if I/O error happens
     */
    private int findBlock(byte[] hash) throws IllegalArgumentException, IOException {
        verifyHash(hash);
        return -1;
    }

    /**
     * Verify hash length.
     *
     * @param hash hash to be verified
     * @throws IllegalArgumentException if hash has incorrect length
     */
    private void verifyHash(byte[] hash) throws IllegalArgumentException {
        if (hash.length != config.hashLength) throw new IllegalArgumentException("hash has incorrect length");
    }

}
