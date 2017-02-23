package ru.opensecreto.crypto.Ed25519;

import java.math.BigInteger;

class Point {
    public BigInteger x;
    public BigInteger y;
    public BigInteger z;
    public BigInteger t;

    public Point(BigInteger x, BigInteger y, BigInteger z, BigInteger t) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + t + ")";
    }
}
