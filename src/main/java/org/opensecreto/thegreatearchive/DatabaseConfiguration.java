package org.opensecreto.thegreatearchive;

import org.opensecreto.thegreatearchive.exceptions.ImmutableFieldException;

public class DatabaseConfiguration {

    private boolean immutable;

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) throws ImmutableFieldException {
        if (immutable) {
            throw new ImmutableFieldException("Can not change fields of immutable object.");
        }
        this.path = path;
    }

    public void setImmutable() {
        immutable = true;
    }

    public boolean isImmutable() {
        return immutable;
    }

}
