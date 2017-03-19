package ru.opensecreto.sigmacoin.crypto.Ed25519;

import ru.opensecreto.crypto.BaseSigner;
import ru.opensecreto.crypto.Util;

import java.math.BigInteger;

public class Ed25519SHA512 implements BaseSigner {

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
        BigInteger a = Ed25519Math.secretExpand(secret).v;
        return Ed25519Math.pointCompress(Ed25519Math.pointMultiply(a, Ed25519Math.G));
    }

    @Override
    public byte[] sign(byte[] message, byte[] privKey) {
        Secret tmp = Ed25519Math.secretExpand(privKey);
        //-------
        BigInteger a = tmp.v;
        byte[] prefix = tmp.arr;
        byte[] A = Ed25519Math.pointCompress(Ed25519Math.pointMultiply(a, Ed25519Math.G));
        BigInteger r = Ed25519Math.sha512_modq(Util.arrayConcat(prefix, message));
        Point R = Ed25519Math.pointMultiply(r, Ed25519Math.G);
        byte[] Rs = Ed25519Math.pointCompress(R);
        BigInteger h = Ed25519Math.sha512_modq(Util.arrayConcat(Util.arrayConcat(Rs, A), message));
        //s = (r + h * a) % q
        BigInteger s = h.multiply(a).add(r).mod(Ed25519Math.q);
        return Util.arrayConcat(Rs, Util.arrayLim(Util.switchEndianness(s.toByteArray()), 32));
    }

    @Override
    public boolean verify(byte[] pubKey, byte[] message, byte[] signature) {
        if (pubKey.length != 32) {
            throw new IllegalArgumentException("Bad public key length");
        }
        if (signature.length != 64) {
            throw new IllegalArgumentException("Bad signature length");
        }
        Point A = Ed25519Math.pointDecompress(pubKey);
        if (A == null) {
            return false;
        }
        byte[] Rs = new byte[32];
        System.arraycopy(signature, 0, Rs, 0, 32);

        Point R = Ed25519Math.pointDecompress(Rs);
        if (R == null) {
            return false;
        }

        byte[] sigR = new byte[32];
        System.arraycopy(signature, 32, sigR, 0, 32);

        BigInteger s = new BigInteger(1, Util.switchEndianness(sigR));
        BigInteger h = Ed25519Math.sha512_modq(Util.arrayConcat(Util.arrayConcat(Rs, pubKey), message));

        Point sB = Ed25519Math.pointMultiply(s, Ed25519Math.G);
        Point hA = Ed25519Math.pointMultiply(h, A);

        return Ed25519Math.pointEquals(sB, Ed25519Math.pointAdd(R, hA));
    }

}
