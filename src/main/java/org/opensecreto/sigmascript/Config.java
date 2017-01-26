package org.opensecreto.sigmascript;

public final class Config {

    /**
     * Max stack size in bytes.
     * Now 16 MiB
     */
    public static final int MAX_STACK = 16 * 1024 * 1024;

    /**
     * Max heap size in bytes.
     * Now 64 MiB
     */
    public static final int MAX_MEMORY = 64 * 1024 * 1024;

    public static final long STORAGE_MAX_SIZE = 0xffffffff;
}
