package ru.opensecreto.sigmacoin.blockstorage.exception;

public class BlockNotFoundException extends Exception {

    public BlockNotFoundException() {
        super();
    }

    public BlockNotFoundException(String message) {
        super(message);
    }

    public BlockNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockNotFoundException(Throwable cause) {
        super(cause);
    }
}
