package ru.opensecreto.sigmacoin.crypto.base;

public interface BaseSigner {

    public Signature sign(byte[] message, PrivateKey privateKey);

    public boolean verify(byte[] message, Signature signature, PublicKey publicKey);

}
