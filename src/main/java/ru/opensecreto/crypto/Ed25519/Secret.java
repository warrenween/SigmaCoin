package ru.opensecreto.crypto.Ed25519;

import java.math.BigInteger;

class Secret {
    public BigInteger v;
    public byte[] arr;

    public Secret(BigInteger a, byte[] h) {
        this.v = a;
        this.arr = h;
    }
}
