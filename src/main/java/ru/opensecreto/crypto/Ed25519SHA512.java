package ru.opensecreto.crypto;

import org.bouncycastle.crypto.digests.SHA512Digest;

import java.math.BigInteger;

public class Ed25519SHA512 implements BaseSigner {

    /**
     * Curve constant
     */
    private static BigInteger d = new BigInteger("37095705934669439343138083508754565189542113879843219016388785533085940283555");
    /**
     * Base field Z_p
     */
    private static BigInteger p = new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564819949");
    /**
     * Group order
     */
    private static BigInteger q = new BigInteger("7237005577332262213973186563042994240857116359379907606001950938285454250989");

    private static BigInteger modp_sqrt_m1 = new BigInteger("19681161376707505956807079304988542015446066515923890162744021073123829784752");

    private static BigInteger g_y = new BigInteger("46316835694926478169428394003475163141307993866256225615783033603165251855960");

    private static BigInteger g_x = new BigInteger("15112221349535400772501151409588531511454012693041857206046113283949847762202");

    private static Point G = new Point(
            new BigInteger("15112221349535400772501151409588531511454012693041857206046113283949847762202"),
            new BigInteger("46316835694926478169428394003475163141307993866256225615783033603165251855960"),
            BigInteger.ONE,
            new BigInteger("46827403850823179245072216630277197565144205554125654976674165829533817101731")
    );

    private static BigInteger modp_inv(BigInteger x) {
        return x.modPow(p.subtract(new BigInteger("2")), p);
    }

    private static byte[] pointCompress(Point P) {
        BigInteger zinv = modp_inv(P.z);
        BigInteger x = P.x.mod(zinv).mod(p);
        BigInteger y = P.y.mod(zinv).mod(p);
        return Util.bigToLittleEndian(y.or(x.and(BigInteger.ONE).shiftLeft(255)).toByteArray());
    }

    private static Point pointDecompress(byte[] s) {
        if (s.length != 32) {
            throw new IllegalArgumentException("s length must be 32");
        }
        BigInteger y = new BigInteger(Util.bigToLittleEndian(s));
        BigInteger sign = y.shiftRight(255);
        y = y.and(new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564819967"));

        BigInteger x = recoverX(y, sign);
        if (x == null) {
            return null;
        } else {
            return new Point(x, y, BigInteger.ONE, x.multiply(y).mod(p));
        }
    }

    private static byte[] sha512(byte[] s) {
        SHA512Digest digest = new SHA512Digest();
        digest.update(s, 0, 32);
        byte[] out = new byte[64];
        digest.doFinal(out, 0);
        return out;
    }

    private static Secret secretExpand(byte[] secret) {
        if (secret.length != 32) {
            throw new IllegalArgumentException("secret must have length of 32");
        }
        byte[] h = Util.bigToLittleEndian(sha512(secret));
        BigInteger a = new BigInteger(h);
        a = a.and(new BigInteger("28948022309329048855892746252171976963317496166410141009864396001978282409976"));
        a = a.or(new BigInteger("28948022309329048855892746252171976963317496166410141009864396001978282409984"));
        return new Secret(a, h);
    }

    /**
     * Computes Q = s * Q
     */
    private static Point pointMultiply(BigInteger s, Point P) {
        Point Q = new Point(BigInteger.ZERO, BigInteger.ONE, BigInteger.ONE, BigInteger.ZERO);
        while (s.compareTo(BigInteger.ZERO) > 0) {
            if (s.and(BigInteger.ONE).compareTo(BigInteger.ZERO) > 0) {
                Q = pointAdd(Q, P);
            }
            P = pointAdd(P, P);
            s = s.shiftRight(1);
        }
        return Q;
    }

    private static boolean pointEquals(Point P, Point Q) {
        //P.x * Q.z - Q.x * P.z) % p != 0
        if (P.x.multiply(Q.z).subtract(Q.x.multiply(P.z)).mod(p).compareTo(BigInteger.ZERO) != 0) {
            return false;
        }
        //P.y * Q.z - Q.y * P.z) % p != 0
        if (P.y.multiply(Q.z).subtract(Q.y.multiply(P.z)).mod(p).compareTo(BigInteger.ZERO) != 0) {
            return false;
        }
        return true;
    }

    private static Point pointAdd(Point P, Point Q) {
        //A = (P.y-P.x)*(Q.y-Q.x) % p
        BigInteger A = P.y.subtract(P.x).multiply(Q.y.subtract(Q.x)).mod(p);
        //B = (P.y+P.x)*(Q.y+Q.x) % p
        BigInteger B = P.y.add(P.x).multiply(Q.y.add(Q.x)).mod(p);
        //C = 2 * P.t * Q.t * d % p
        BigInteger C = new BigInteger("2").multiply(P.t).multiply(Q.t).multiply(d).mod(p);
        //D = 2 * P.z * Q.z % p
        BigInteger D = new BigInteger("2").max(P.z).multiply(Q.z).mod(p);
        //E = B-A
        BigInteger E = B.subtract(A);
        //F = D-C
        BigInteger F = D.subtract(C);
        //G = D+C
        BigInteger G = D.add(C);
        //H = B+A
        BigInteger H = B.add(A);
        //return (E*F, G*H, F*G, E*H)
        return new Point(E.multiply(F), G.multiply(H), F.multiply(G), E.multiply(H));
    }

    private static BigInteger recoverX(BigInteger y, BigInteger sign) {
        //x2 = (y*y-1) * modp_inv(d*y*y+1)
        BigInteger x2 = y.multiply(y).subtract(BigInteger.ONE).multiply(modp_inv(d.multiply(y).multiply(y).add(BigInteger.ONE)));
        if (x2.compareTo(BigInteger.ZERO) == 0) {
            if (sign.compareTo(BigInteger.ZERO) == 0) {
                return null;
            } else {
                return BigInteger.ZERO;
            }
        }

        BigInteger x = x2.modPow(new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564819952"), p);
        if (x.multiply(x).subtract(x2).mod(p).compareTo(BigInteger.ZERO) != 0) {
            x = x.multiply(modp_sqrt_m1).mod(p);
        }
        if (x.multiply(x).subtract(x2).mod(p).compareTo(BigInteger.ZERO) != 0) {
            return null;
        }

        if (x.and(BigInteger.ONE).compareTo(sign) != 0) {
            x = x.subtract(p);
        }
        return x;
    }

    /**
     * Generate public key from 32 bytes of private key.
     * <p>
     * See: EdDSA and Ed25519 draft-josefsson-eddsa-ed25519-03
     * <p>
     * https://tools.ietf.org/html/draft-josefsson-eddsa-ed25519-03
     *
     * @param secret private key of 32 bytes
     * @return public key for this key
     */
    public static byte[] getPublicKey(byte[] secret) {
        BigInteger a = secretExpand(secret).v;
        return pointCompress(pointMultiply(a, G));
    }

    @Override
    public byte[] sign(byte[] message, byte[] privKey) {
        return new byte[0];
    }

    @Override
    public boolean verify(byte[] message, byte[] signature, byte[] pubKey) {
        return false;
    }

    private static class Point {
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
    }

    private static class Secret {
        public BigInteger v;
        public byte[] arr;

        public Secret(BigInteger a, byte[] h) {
            this.v = a;
            this.arr = h;
        }
    }
}
