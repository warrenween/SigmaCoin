package org.opensecreto.TheGreatBlockchainArchive;

import org.opensecreto.TheGreatBlockchainArchive.exceptions.ImmutableFieldException;

public class BlockchainArchiveConfiguration {

    private boolean immutable;

    private String indexFile;
    private String blockchainFile;
    private int hashLength;

    public String getIndexFile() {
        return indexFile;
    }

    public void setIndexFile(String indexFile) throws ImmutableFieldException {
        failImmutable();
        this.indexFile = indexFile;
    }

    public String getBlockchainFile() throws ImmutableFieldException {
        return blockchainFile;
    }

    public void setBlockchainFile(String blockchainFile) {
        failImmutable();
        this.blockchainFile = blockchainFile;
    }

    public int getHashLength() {
        return hashLength;
    }

    public void setHashLength(int hashLength) throws ImmutableFieldException {
        failImmutable();
        this.hashLength = hashLength;
    }

    public void setImmutable() {
        immutable = true;
    }

    public boolean isImmutable() {
        return immutable;
    }

    private void failImmutable() {
        if (immutable) {
            throw new ImmutableFieldException("Can not change fields of immutable object.");
        }
    }

}
