package ru.opensecreto.sigmacoin.crypto.Ed25519;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.checkNotNull;

class Point {

    public BigInteger x;
    public BigInteger y;
    public BigInteger z;
    public BigInteger t;

    public Point(BigInteger x, BigInteger y, BigInteger z, BigInteger t) throws NullPointerException {
        this.x = checkNotNull(x);
        this.y = checkNotNull(y);
        this.z = checkNotNull(z);
        this.t = checkNotNull(t);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + t + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Point) &&
                ((Point) obj).x.equals(x) && ((Point) obj).y.equals(y) &&
                ((Point) obj).z.equals(z) && ((Point) obj).t.equals(t);
    }
}
