package ru.opensecreto.sigmacoin.time;

/**
 * Class for handling timestamps.
 * Timestamp - number of milliseconds since 01-01-1970T00:00Z
 */
public class Time {

    /**
     * Offset from system time;
     */
    private volatile long offset;

    public long getTime() {
        return System.currentTimeMillis() + offset;
    }

    public void setOffset(long newOffset) {
        offset = newOffset;
    }

}
