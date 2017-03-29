package ru.opensecreto.sigmacoin.blockchain;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * Important: controller is NOT thread safe, also it is NOT protected from corrupted index and blockchain files
 */
public class BlockchainController {

    public BlockchainConfiguration configuration;
    private RandomAccessFile index;
    private RandomAccessFile blockchain;

    public BlockchainController(BlockchainConfiguration configuration) throws IOException {
        this.configuration = configuration;

        index = new RandomAccessFile(configuration.indexFile, "rwd");
        blockchain = new RandomAccessFile(configuration.blockchainFile, "rwd");
    }

    public byte[] get(byte[] hash) throws IOException {
        BlockIndex index = findHash(hash);
        if (index == null) {
            return null;
        } else {
            blockchain.seek(index.offset);
            byte[] data = new byte[index.length];
            blockchain.read(data, 0, index.length);
            return data;
        }
    }

    /**
     * Deletes block with given hash.
     * Notice: block is not actually deleted! Given index in index file is marked as invalid, though it
     * can be overwritten. Data in blockchain file is not deleted.
     *
     * @param hash
     * @return True if delete was successful, otherwise false
     */
    public boolean delete(byte[] hash) throws IOException {
        BlockIndex index = findHash(hash);
        if (index == null) {
            return false;
        } else {
            this.index.seek(index.indexOffset);
            this.index.writeBoolean(false);
            return true;
        }
    }

    public void put(byte[] hash, byte[] data) throws IOException {
        if (hash.length != configuration.hashLength) {
            throw new IllegalArgumentException("Given hash length " + hash.length + " " +
                    "is not equal to configuration length " + configuration.hashLength + ".");
        }

        //searching for free space in index file
        long indexOffset = index.length();
        index.seek(0);
        boolean search = true;
        while ((index.getFilePointer() < index.length()) && search) {
            long currentOffset = index.getFilePointer();
            if (!index.readBoolean()) {
                search = false;
                //we read one byte with index.readBoolean() that's why we should do -1 indexGetFilePointer
                indexOffset = currentOffset;
            }
            index.skipBytes(configuration.hashLength + Integer.BYTES + Long.BYTES);
        }

        long blockchainOffset = blockchain.length();
        blockchain.seek(blockchainOffset);
        blockchain.write(data);

        this.index.seek(indexOffset);
        this.index.writeBoolean(true);
        this.index.write(hash);
        this.index.writeLong(blockchainOffset);
        this.index.writeInt(data.length);
    }

    private BlockIndex findHash(byte[] hash) throws IOException {
        index.seek(0);
        byte[] tempHash = new byte[configuration.hashLength];
        while (index.getFilePointer() < index.length()) {
            long indexOffset = index.getFilePointer();
            boolean valid = index.readBoolean();
            index.read(tempHash);
            long offset = index.readLong();
            int length = index.readInt();

            if (Arrays.equals(tempHash, hash) && valid) {
                return new BlockIndex(hash, offset, length, indexOffset);
            }
        }
        return null;
    }

    /**
     * Removes junk data from blockchain file.
     * Because {@link BlockchainController#delete(byte[])} only marks index entry as invalid
     * and don't deletes data from blockchain file (it costs too much resources), you need call reindex periodically.
     * <p>
     * This will copy old index and blockchain files and move valid entries to new index and blockchain files.
     */
    public void reindex() throws IOException {
        //close old files
        index.close();
        blockchain.close();

        File oldIndexFile = new File(configuration.indexFile + ".old");
        File oldBlockchainFile = new File(configuration.blockchainFile + ".old");
        File newIndexFile = new File(configuration.indexFile);
        File newBlockchainFile = new File(configuration.blockchainFile);

        newIndexFile.renameTo(oldIndexFile);
        newBlockchainFile.renameTo(oldBlockchainFile);

        RandomAccessFile oldIndexFileRAF = new RandomAccessFile(oldIndexFile, "r");
        RandomAccessFile oldBlockchainRAF = new RandomAccessFile(oldBlockchainFile, "r");
        RandomAccessFile newIndexFileRAF = new RandomAccessFile(newIndexFile, "rwd");
        RandomAccessFile newBlockchainFileRAF = new RandomAccessFile(newBlockchainFile, "rwd");

        byte[] hash = new byte[configuration.hashLength];
        while (oldIndexFileRAF.getFilePointer() < oldBlockchainRAF.length()) {
            boolean oldValid = oldIndexFileRAF.readBoolean();
            oldIndexFileRAF.read(hash);
            long oldOffset = oldIndexFileRAF.readLong();
            int oldLength = oldIndexFileRAF.readInt();

            if (oldValid) {
                //write data
                byte[] data = new byte[oldLength];
                oldBlockchainRAF.seek(oldOffset);
                oldBlockchainRAF.read(data, 0, oldLength);
                long newOffset = newBlockchainFileRAF.getFilePointer();
                newBlockchainFileRAF.write(data);

                newIndexFileRAF.writeBoolean(true);
                newIndexFileRAF.write(hash);
                newIndexFileRAF.writeLong(newOffset);
                newIndexFileRAF.writeInt(oldLength);
            }
        }

        oldBlockchainRAF.close();
        oldBlockchainFile.delete();
        oldIndexFileRAF.close();
        oldIndexFile.delete();

        index = newIndexFileRAF;
        blockchain = newBlockchainFileRAF;
    }

    private static class BlockIndex {
        public final byte[] hash;
        public final long offset;
        public final int length;
        public final long indexOffset;

        public BlockIndex(byte[] hash, long offset, int length, long indexOffset) {
            this.hash = hash;
            this.offset = offset;
            this.length = length;
            this.indexOffset = indexOffset;
        }
    }

}
