package ru.opensecreto.sigmacoin.crypto;

public interface BaseSigner {

    public byte[] sign(byte[] message, PrivateKey privateKey);

    public boolean verify(byte[] message, byte[] signature, PublicKey publicKey);

}
