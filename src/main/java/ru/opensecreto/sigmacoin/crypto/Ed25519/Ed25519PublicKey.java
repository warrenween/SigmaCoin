package ru.opensecreto.sigmacoin.crypto.Ed25519;

import ru.opensecreto.sigmacoin.crypto.PublicKey;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Ed25519PublicKey implements PublicKey {

    private static final int PUBLIC_KEY_LENGTH = 32;

    private final byte[] key;

    public Ed25519PublicKey(byte[] key) {
        this.key = Arrays.copyOf(checkNotNull(key), PUBLIC_KEY_LENGTH);
        checkArgument(key.length == PUBLIC_KEY_LENGTH, "Private key must have length 32");
    }

    public byte[] getPublicKey() {
        return Arrays.copyOf(key, 32);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Ed25519PublicKey) &&
                Arrays.equals(((Ed25519PublicKey) obj).key, key);
    }
}
