package ru.opensecreto.sigmacoin.crypto.Ed25519;

import ru.opensecreto.sigmacoin.crypto.PublicKey;

import java.util.Arrays;

public class Ed25519PublicKey implements PublicKey {

    private final byte[] key;

    public Ed25519PublicKey(byte[] key) {
        if (key == null) throw new IllegalArgumentException("key is null");
        if (key.length != 32) throw new IllegalArgumentException(String.format("Key length 32. Given {}.", key.length));

        this.key = Arrays.copyOf(key, 32);
    }

    public byte[] getPublicKey() {
        return Arrays.copyOf(key, 32);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Ed25519PublicKey) &&
                (Arrays.equals(((Ed25519PublicKey) obj).key, key));
    }
}
