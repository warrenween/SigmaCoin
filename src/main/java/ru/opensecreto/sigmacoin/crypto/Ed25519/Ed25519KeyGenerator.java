package ru.opensecreto.sigmacoin.crypto.Ed25519;

import ru.opensecreto.sigmacoin.crypto.base.Keypair;
import ru.opensecreto.sigmacoin.crypto.base.PrivateKey;
import ru.opensecreto.sigmacoin.crypto.base.PublicKey;

import java.math.BigInteger;
import java.security.SecureRandom;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static ru.opensecreto.sigmacoin.crypto.Ed25519.Ed25519SHA512.ED25519SHA512_ID;

public class Ed25519KeyGenerator {

    private final SecureRandom secureRandom;

    public Ed25519KeyGenerator() {
        this(new SecureRandom(SecureRandom.getSeed(32)));
    }

    public Ed25519KeyGenerator(SecureRandom secureRandom) throws NullPointerException {
        this.secureRandom = checkNotNull(secureRandom);
    }

    public PrivateKey generatePrivateKey() {
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return new PrivateKey(ED25519SHA512_ID, key);
    }

    public static Keypair generateKeypair(PrivateKey privateKey)
            throws NullPointerException, IllegalArgumentException {
        checkNotNull(privateKey);
        checkArgument(privateKey.getMethod() == ED25519SHA512_ID);

        PublicKey publicKey = generatePublicKey(privateKey);
        return new Keypair(publicKey, privateKey);
    }

    public Keypair generateKeypair() {
        return generateKeypair(generatePrivateKey());
    }

    public static PublicKey generatePublicKey(PrivateKey privateKey)
            throws NullPointerException, IllegalArgumentException {
        checkNotNull(privateKey);
        checkArgument(privateKey.getMethod() == ED25519SHA512_ID);

        BigInteger a = Ed25519Math.secretExpand(privateKey.getPrivateKey()).v;
        return new PublicKey(ED25519SHA512_ID, Ed25519Math.pointCompress(Ed25519Math.pointMultiply(a, Ed25519Math.G)));
    }
}
