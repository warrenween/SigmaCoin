package ru.opensecreto.sigmacoin.crypto.Ed25519;

import ru.opensecreto.openutil.Util;
import ru.opensecreto.sigmacoin.crypto.BaseSigner;
import ru.opensecreto.sigmacoin.crypto.PrivateKey;
import ru.opensecreto.sigmacoin.crypto.PublicKey;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * See: EdDSA and Ed25519 draft-josefsson-eddsa-ed25519-03
 * <p>
 * https://tools.ietf.org/html/draft-josefsson-eddsa-ed25519-03
 */
public class Ed25519SHA512 implements BaseSigner {

    @Override
    public byte[] sign(byte[] message, PrivateKey privateKey) {
        if (!(privateKey instanceof Ed25519PrivateKey))
            throw new IllegalArgumentException(String.format(
                    "Private key must be '{}'. Given '{}'",
                    Ed25519PrivateKey.class, privateKey.getClass()
            ));

        Secret tmp = Ed25519Math.secretExpand(((Ed25519PrivateKey) privateKey).getPrivateKey());
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
        return Util.arrayConcat(Rs, Arrays.copyOf(Util.switchEndianness(s.toByteArray()), 32));
    }

    @Override
    public boolean verify(byte[] message, byte[] signature, PublicKey publicKey) {
        if (!(publicKey instanceof Ed25519PublicKey))
            throw new IllegalArgumentException(String.format(
                    "Private key must be '{}'. Given '{}'",
                    Ed25519PublicKey.class, publicKey.getClass()
            ));

        if (signature.length != 64) {
            throw new IllegalArgumentException("Bad signature length");
        }
        Point A = Ed25519Math.pointDecompress(((Ed25519PublicKey) publicKey).getPublicKey());
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
        BigInteger h = Ed25519Math.sha512_modq(Util.arrayConcat(
                Util.arrayConcat(Rs, ((Ed25519PublicKey) publicKey).getPublicKey()), message)
        );

        Point sB = Ed25519Math.pointMultiply(s, Ed25519Math.G);
        Point hA = Ed25519Math.pointMultiply(h, A);

        return Ed25519Math.pointEquals(sB, Ed25519Math.pointAdd(R, hA));
    }

}
