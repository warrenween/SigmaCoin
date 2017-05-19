package ru.opensecreto.sigmacoin.config;

import org.bouncycastle.crypto.digests.SHA3Digest;
import ru.opensecreto.sigmacoin.core.DigestProvider;

public class CoinConfig {

    public static final String COIN_NAME = "Sigmacoin";
    public static final String COIN_SHORT_NAME = "SGC";

    /**
     * 16 MiB
     */
    public static final int MAX_BLOCK_SIZE = 16 * 1024 * 1024;

    /**
     * Digits after dot.
     */
    public static final int PRECISION = 15;

    /**
     * SHA3-512
     */
    public static final int HASH_LENGTH = 64;

    public final static DigestProvider POW_DIGEST = () -> new SHA3Digest(512);

    public final static int POW_N = 3;

    public static final int SVM_STACK_SIZE = 8192;

    public static final long SVM_MEMORY_SIZE = 16384;

    /**
     * Length of contract id in bytes.
     */
    public static final int CONTRACT_ID_LENGTH = 32;

    public static final int SVM_MAX_CALL_STACK_DEPTH = 10;

}
