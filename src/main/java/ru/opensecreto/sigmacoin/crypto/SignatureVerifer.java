package ru.opensecreto.sigmacoin.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.crypto.base.Signer;
import ru.opensecreto.sigmacoin.crypto.base.PublicKey;
import ru.opensecreto.sigmacoin.crypto.base.Signature;

import static com.google.common.base.Preconditions.checkNotNull;

public class SignatureVerifer {

    public static final Logger LOGGER = LoggerFactory.getLogger(SignatureVerifer.class);

    public boolean verify(byte[] message, Signature signature, PublicKey publicKey) throws NullPointerException {
        checkNotNull(message);
        checkNotNull(signature);
        checkNotNull(publicKey);

        if (!Signers.SIGNER_FACTORIES.containsKey(signature.getMethod())) {
            LOGGER.warn("Unknown signature id {}.", signature.getMethod());
            return false;
        }

        Signer signer = Signers.SIGNER_FACTORIES.get(signature.getMethod()).getSigner();
        return signer.verify(message, signature, publicKey);
    }

}
