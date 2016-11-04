package org.opensecreto.sigmascipt.exceptions;

public class BaseScriptException extends Exception {

    public BaseScriptException() {
        super();
    }

    public BaseScriptException(String message) {
        super(message);
    }

    public BaseScriptException(String message, Throwable cause) {
        super(message, cause);
    }
}
