package ru.opensecreto.sigmacoin.crypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.sigmacoin.crypto.Ed25519.Ed25519SHA512;
import ru.opensecreto.sigmacoin.crypto.base.Signer;
import ru.opensecreto.sigmacoin.crypto.base.PrivateKey;
import ru.opensecreto.sigmacoin.crypto.base.PublicKey;
import ru.opensecreto.sigmacoin.crypto.base.Signature;
import ru.opensecreto.sigmacoin.crypto.empty.EmptySigner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class Signers implements Signer {

    public static final Map<Integer, SignerFactory> SIGNER_FACTORIES = Collections.unmodifiableMap(new HashMap<Integer, SignerFactory>() {{
        put(EmptySigner.EMPTYSIGNER_ID, EmptySigner::new);
        put(Ed25519SHA512.ED25519SHA512_ID, Ed25519SHA512::new);
    }});

    @FunctionalInterface
    public static interface SignerFactory {

        public Signer getSigner();

    }

    @Override
    public Signature sign(byte[] message, PrivateKey privateKey) throws NullPointerException, UnsupportedOperationException {
        checkNotNull(message);
        checkNotNull(privateKey);

        if (!SIGNER_FACTORIES.containsKey(privateKey.getMethod())) {
            throw new UnsupportedOperationException("Can not sign - unknown method " + privateKey.getMethod());
        }

        Signer signer = SIGNER_FACTORIES.get(privateKey.getMethod()).getSigner();
        return signer.sign(message, privateKey);
    }

    public static final Logger LOGGER = LoggerFactory.getLogger(Signers.class);

    @Override
    public boolean verify(byte[] message, Signature signature, PublicKey publicKey) throws NullPointerException {
        checkNotNull(message);
        checkNotNull(signature);
        checkNotNull(publicKey);

        if (!SIGNER_FACTORIES.containsKey(publicKey.getMethod())) {
            return false;
        }

        try {
            Signer signer = SIGNER_FACTORIES.get(publicKey.getMethod()).getSigner();
            return signer.verify(message, signature, publicKey);
        } catch (Throwable e) {
            LOGGER.warn("Exception when verifying signature", e);
            return false;
        }
    }

}
