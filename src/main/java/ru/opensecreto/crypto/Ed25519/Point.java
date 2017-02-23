package ru.opensecreto.crypto.Ed25519;

import java.math.BigInteger;

class Point {
    public BigInteger x;
    public BigInteger y;
    public BigInteger z;
    public BigInteger t;

    public Point(BigInteger x, BigInteger y, BigInteger z, BigInteger t) {
        if (x == null) {
            throw new NullPointerException("null arguments are not allowed");
        }
        if (y == null) {
            throw new NullPointerException("null arguments are not allowed");
        }
        if (z == null) {
            throw new NullPointerException("null arguments are not allowed");
        }
        if (t == null) {
            throw new NullPointerException("null arguments are not allowed");
        }
        this.x = x;
        this.y = y;
        this.z = z;
        this.t = t;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ", " + t + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Point) && (this == obj) &&
                (((Point) obj).x.equals(x)) && (((Point) obj).y.equals(y)) &&
                (((Point) obj).z.equals(z)) && (((Point) obj).t.equals(t));
    }
}
