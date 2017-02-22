package ru.opensecreto.crypto;

import org.bouncycastle.crypto.digests.SHA512Digest;

public class Ed25519KeyGenerator {

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
        if (secret.length != 32) {
            throw new IllegalArgumentException("secret must have length of 32");
        }
        SHA512Digest digest = new SHA512Digest();
        digest.update(secret, 0, 32);
        byte[] h = new byte[64];
        digest.doFinal(h, 0);

        h[0] &= 0x07;
        h[31] &= 0x7F;
        h[31] |= 0x40;


        return new byte[0];
    }

}
