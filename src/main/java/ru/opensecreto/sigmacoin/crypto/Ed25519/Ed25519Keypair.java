package ru.opensecreto.sigmacoin.crypto.Ed25519;

public class Ed25519Keypair {

    public final Ed25519PrivateKey privateKey;
    public final Ed25519PublicKey publicKey;

    public Ed25519Keypair(Ed25519PrivateKey privateKey, Ed25519PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
}
