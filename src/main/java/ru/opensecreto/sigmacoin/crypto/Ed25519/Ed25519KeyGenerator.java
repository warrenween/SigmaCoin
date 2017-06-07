package ru.opensecreto.sigmacoin.crypto.Ed25519;

import java.math.BigInteger;
import java.security.SecureRandom;

import static com.google.common.base.Preconditions.checkNotNull;

public class Ed25519KeyGenerator {

    private final SecureRandom secureRandom;

    public Ed25519KeyGenerator() {
        this(new SecureRandom(SecureRandom.getSeed(32)));
    }

    public Ed25519KeyGenerator(SecureRandom secureRandom) {
        this.secureRandom = checkNotNull(secureRandom);
    }

    public Ed25519PrivateKey generatePrivateKey() {
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return new Ed25519PrivateKey(key);
    }

    public static Ed25519Keypair generateKeypair(Ed25519PrivateKey privateKey) {
        Ed25519PublicKey publicKey = generatePublicKey(privateKey);
        return new Ed25519Keypair(privateKey, publicKey);
    }

    public Ed25519Keypair generateKeypair() {
        return generateKeypair(generatePrivateKey());
    }

    public static Ed25519PublicKey generatePublicKey(Ed25519PrivateKey privateKey) {
        BigInteger a = Ed25519Math.secretExpand(checkNotNull(privateKey).getPrivateKey()).v;
        return new Ed25519PublicKey(Ed25519Math.pointCompress(Ed25519Math.pointMultiply(a, Ed25519Math.G)));
    }
}
