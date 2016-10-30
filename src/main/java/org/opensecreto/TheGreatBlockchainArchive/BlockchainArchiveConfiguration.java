package org.opensecreto.TheGreatBlockchainArchive;

import org.opensecreto.TheGreatBlockchainArchive.exceptions.ImmutableFieldException;

public class BlockchainArchiveConfiguration {

    private boolean immutable;

    private String path;
    private int hashLength;

    public String getPath() {
        return path;
    }

    public void setPath(String path) throws ImmutableFieldException {
        if (immutable) {
            throw new ImmutableFieldException("Can not change fields of immutable object.");
        }
        this.path = path;
    }

    public int getHashLength() {
        return hashLength;
    }

    public void setHashLength(int hashLength) throws ImmutableFieldException {
        if (immutable) {
            throw new ImmutableFieldException("Can not change fields of immutable object.");
        }
        this.hashLength = hashLength;
    }

    public void setImmutable() {
        immutable = true;
    }

    public boolean isImmutable() {
        return immutable;
    }

}
