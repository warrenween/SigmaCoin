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

    private static byte[] sha512(byte[] s) {
        SHA512Digest digest = new SHA512Digest();
        digest.update(s, 0, 32);
        byte[] out = new byte[32];
        digest.doFinal(out, 0);
        return out;
    }

    private static BigInteger secretExpand(byte[] secret) {
        if (secret.length != 32) {
            throw new IllegalArgumentException("secret must have length of 32");
        }
        byte[] h = sha512(secret);
        h = Util.bigToLittleEndian(h);
        BigInteger a = new BigInteger(h);
        a = a.and(new BigInteger("28948022309329048855892746252171976963317496166410141009864396001978282409976"));
        a = a.or(new BigInteger("28948022309329048855892746252171976963317496166410141009864396001978282409984"));
        return a;
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

        SHA512Digest digest = new SHA512Digest();
        digest.update(secret, 0, 32);
        byte[] h = new byte[64];
        digest.doFinal(h, 0);

        h[0] &= 0x07;
        h[31] &= 0x7F;
        h[31] |= 0x40;


        return new byte[0];
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
}
