package org.opensecreto.TheGreatBlockchainArchive.exceptions;

public class ImmutableFieldException extends Exception {

    public ImmutableFieldException() {
        super();
    }

    public ImmutableFieldException(String message) {
        super(message);
    }

    public ImmutableFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImmutableFieldException(Throwable cause) {
        super(cause);
    }

    protected ImmutableFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
