package ru.opensecreto.sigmacoin.crypto;

import ru.opensecreto.sigmacoin.crypto.Ed25519.Ed25519SHA512;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Signers {

    public static final Map<Short, BaseSigner> SIGNERS = Collections.unmodifiableMap(new HashMap<Short, BaseSigner>() {{
        put((short) 0, new Ed25519SHA512());
    }});

    public static final Map<Short, String> SIGNERS_NAMES = Collections.unmodifiableMap(new HashMap<Short, String>() {{
        put((short) 0, "Ed25519-SHA512");
    }});

    public static final Map<Short, Integer> SIG_SIZES = Collections.unmodifiableMap(new HashMap<Short, Integer>() {{
        put((short) 0, 64);
    }});

}
