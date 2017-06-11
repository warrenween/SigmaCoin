package ru.opensecreto.sigmacoin.crypto.base;

public interface Signer {

    /**
     * Sign message with given private key.
     *
     * @param message    message to sign
     * @param privateKey private key to sign with
     * @return signature for this message
     * @throws NullPointerException          if any of arguments is null
     * @throws UnsupportedOperationException if this signer do not support method private key method
     */
    public Signature sign(byte[] message, PrivateKey privateKey)
            throws NullPointerException, UnsupportedOperationException;

    /**
     * Verify signature for message.
     * Note:
     * <ul>
     * <li>if method of signature is unsupported, exception is not thrown, false is returned</li>
     * <li>All exception with other reasons than in this document are suppressed and false is returned</li>
     * </ul>
     *
     * @param message   message for signature.
     * @param signature signature to verify
     * @param publicKey public ley to verify signature with.
     * @return true if signature is valid, false otherwise.
     * @throws NullPointerException          if any of arguments is null
     * @throws UnsupportedOperationException if this signer do not support method of public key
     */
    public boolean verify(byte[] message, Signature signature, PublicKey publicKey)
            throws NullPointerException, UnsupportedOperationException;

}
