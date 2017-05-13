package ru.opensecreto.sigmacoin.core.config;

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

    public DigestProvider POW_DIGEST = () -> new SHA3Digest(512);

    public final static int POW_N = 3;

}
