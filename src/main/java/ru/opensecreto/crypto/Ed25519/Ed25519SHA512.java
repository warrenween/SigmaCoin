package ru.opensecreto.crypto.Ed25519;

import ru.opensecreto.crypto.BaseSigner;

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
        return new byte[0];
    }

    @Override
    public boolean verify(byte[] message, byte[] signature, byte[] pubKey) {
        return false;
    }

}
