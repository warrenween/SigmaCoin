package ru.opensecreto.sigmacoin.crypto.Ed25519;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkNotNull;

class Secret {
    public BigInteger v;
    public byte[] arr;

    public Secret(BigInteger a, byte[] h) {
        this.v = checkNotNull(a);
        this.arr = checkNotNull(h);
    }
}
