package ru.opensecreto.sigmacoin.config;

import org.bouncycastle.crypto.digests.SHA3Digest;
import ru.opensecreto.sigmacoin.core.DigestProvider;

public class CoinConfig {

    public static final String COIN_NAME = "Sigmacoin";
    public static final String COIN_SHORT_NAME = "SGC";

    public final static DigestProvider POW_DIGEST = () -> new SHA3Digest(512);
    public final static DigestProvider DEFAULT_DIGEST_PROVIDER = () -> new SHA3Digest(256);


    public final static int POW_N = 3;

    public static final int SVM_MAX_CALL_STACK_DEPTH = 10;

}
