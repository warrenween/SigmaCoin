package ru.opensecreto.sigmacoin.crypto.Ed25519;

import ru.opensecreto.sigmacoin.crypto.PrivateKey;

import java.util.Arrays;

public class Ed25519PrivateKey implements PrivateKey {

    private final byte[] key;

    public Ed25519PrivateKey(byte[] key) {
        if (key == null) throw new IllegalArgumentException("key is null");
        if (key.length != 32) throw new IllegalArgumentException(String.format("Key length 32. Given {0}.", key.length));

        this.key = Arrays.copyOf(key, 32);
    }

    public byte[] getPrivateKey() {
        return Arrays.copyOf(key, 32);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Ed25519PrivateKey) &&
                (Arrays.equals(((Ed25519PrivateKey) obj).key, key));
    }
}
