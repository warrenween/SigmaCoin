package ru.opensecreto.sigmacoin.crypto.Ed25519;

import ru.opensecreto.sigmacoin.crypto.base.Keypair;
import ru.opensecreto.sigmacoin.crypto.base.PrivateKey;
import ru.opensecreto.sigmacoin.crypto.base.PublicKey;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Ed25519Keypair implements Keypair {

    private final Ed25519PrivateKey privateKey;
    private final Ed25519PublicKey publicKey;

    public Ed25519Keypair(Ed25519PrivateKey privateKey, Ed25519PublicKey publicKey) {
        this.privateKey = checkNotNull(privateKey);
        this.publicKey = checkNotNull(publicKey);
    }

    @Override
    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
