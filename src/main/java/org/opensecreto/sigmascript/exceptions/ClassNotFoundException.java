package org.opensecreto.sigmascript.exceptions;

public class ClassNotFoundException extends BaseScriptException {

    public ClassNotFoundException() {
        super();
    }

    public ClassNotFoundException(String message) {
        super(message);
    }

    public ClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
