package ru.opensecreto.sigmacoin.crypto.Ed25519;

import ru.opensecreto.sigmacoin.crypto.base.PrivateKey;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Ed25519PrivateKey implements PrivateKey {

    private static final int PRIVATE_KEY_LENGTH = 32;

    private final byte[] key;

    public Ed25519PrivateKey(byte[] key) {
        this.key = Arrays.copyOf(checkNotNull(key), PRIVATE_KEY_LENGTH);
        checkArgument(key.length == PRIVATE_KEY_LENGTH, "Private key must have length 32");
    }

    public byte[] getPrivateKey() {
        return Arrays.copyOf(key, PRIVATE_KEY_LENGTH);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Ed25519PrivateKey) &&
                Arrays.equals(((Ed25519PrivateKey) obj).key, key);
    }
}
