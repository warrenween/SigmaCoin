package ru.opensecreto.sigmacoin.crypto;

public interface Keypair {

    public PublicKey getPublicKey();

    public PrivateKey getPrivateKey();

}
