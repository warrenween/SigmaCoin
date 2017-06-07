package ru.opensecreto.sigmacoin.crypto;

import ru.opensecreto.sigmacoin.crypto.Ed25519.Ed25519SHA512;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Signers {

    public static final Map<BigInteger, BaseSigner> SIGNERS = Collections.unmodifiableMap(new HashMap<BigInteger, BaseSigner>() {{
        put(BigInteger.valueOf(1), new Ed25519SHA512());
    }});

    public static final Map<BigInteger, String> SIGNERS_NAMES = Collections.unmodifiableMap(new HashMap<BigInteger, String>() {{
        put(BigInteger.valueOf(1), "Ed25519-SHA512");
    }});

    public static final Map<BigInteger, Integer> SIG_SIZES = Collections.unmodifiableMap(new HashMap<BigInteger, Integer>() {{
        put(BigInteger.valueOf(1), 64);
    }});

}
