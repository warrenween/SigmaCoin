package ru.opensecreto.sigmacoin.vm;

/**
 * This exception is thrown when contract need to stop immediately and all changes are reverted.
 */
public class AbortException extends Exception {

    public AbortException() {
    }

}
