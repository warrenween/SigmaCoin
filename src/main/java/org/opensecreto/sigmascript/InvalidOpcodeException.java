package org.opensecreto.sigmascript;

public class InvalidOpcodeException extends RuntimeException {

    public InvalidOpcodeException() {
        super();
    }

    public InvalidOpcodeException(String message) {
        super(message);
    }

    public InvalidOpcodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOpcodeException(Throwable cause) {
        super(cause);
    }
}
