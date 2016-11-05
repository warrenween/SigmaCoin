package org.opensecreto.sigmascript.exceptions;

public class CastException extends IllegalOperationException {

    public CastException() {
        super();
    }

    public CastException(String message) {
        super(message);
    }

    public CastException(String message, Throwable cause) {
        super(message, cause);
    }
}
