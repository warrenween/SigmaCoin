package ru.opensecreto.sigmacoin.crypto;

public interface BaseSigner {

    public byte[] sign(byte[] message, byte[] privKey);

    public boolean verify(byte[] message, byte[] signature, byte[] pubKey);

}