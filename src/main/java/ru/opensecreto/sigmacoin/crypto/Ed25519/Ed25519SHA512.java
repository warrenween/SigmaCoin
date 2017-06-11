package ru.opensecreto.sigmacoin.crypto.Ed25519;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.opensecreto.openutil.Util;
import ru.opensecreto.sigmacoin.crypto.base.Signer;
import ru.opensecreto.sigmacoin.crypto.base.PrivateKey;
import ru.opensecreto.sigmacoin.crypto.base.PublicKey;
import ru.opensecreto.sigmacoin.crypto.base.Signature;

import java.math.BigInteger;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * See: EdDSA and Ed25519 draft-josefsson-eddsa-ed25519-03
 * <p>
 * https://tools.ietf.org/html/draft-josefsson-eddsa-ed25519-03
 */
public class Ed25519SHA512 implements Signer {

    public static final Logger LOGGER = LoggerFactory.getLogger(Ed25519SHA512.class);
    public static final int ED25519SHA512_ID = 1;

    public static final int SIGNATURE_SIZE = 64;

    @Override
    public Signature sign(byte[] message, PrivateKey privateKey)
            throws NullPointerException, UnsupportedOperationException {
        checkNotNull(message);
        checkNotNull(privateKey);
        if (privateKey.getMethod() != ED25519SHA512_ID) {
            throw new UnsupportedOperationException("Invalid method " + privateKey.getMethod());
        }


        Secret tmp = Ed25519Math.secretExpand(privateKey.getPrivateKey());
        //-------
        BigInteger a = tmp.v;
        byte[] prefix = tmp.arr;
        byte[] A = Ed25519Math.pointCompress(Ed25519Math.pointMultiply(a, Ed25519Math.G));
        BigInteger r = Ed25519Math.sha512_modq(Util.arrayConcat(prefix, message));
        Point R = Ed25519Math.pointMultiply(r, Ed25519Math.G);
        byte[] Rs = Ed25519Math.pointCompress(R);
        BigInteger h = Ed25519Math.sha512_modq(Util.arrayConcat(Util.arrayConcat(Rs, A), message));
        //s = (r + h * a) % q
        BigInteger s = h.multiply(a).add(r).mod(Ed25519Math.q);
        return new Signature(ED25519SHA512_ID, Util.arrayConcat(Rs, Arrays.copyOf(Util.switchEndianness(s.toByteArray()), 32)));
    }

    @Override
    public boolean verify(byte[] message, Signature signature, PublicKey publicKey)
            throws NullPointerException, UnsupportedOperationException {
        checkNotNull(message);
        checkNotNull(signature);
        checkNotNull(publicKey);

        if (publicKey.getMethod() != ED25519SHA512_ID) {
            throw new UnsupportedOperationException();
        }

        if (signature.getMethod() != ED25519SHA512_ID) {
            return false;
        }
        if (signature.getSignature().length != SIGNATURE_SIZE) {
            return false;
        }

        try {
            Point A = Ed25519Math.pointDecompress(publicKey.getPublicKey());
            if (A == null) {
                return false;
            }
            byte[] Rs = new byte[32];
            System.arraycopy(signature.getSignature(), 0, Rs, 0, 32);

            Point R = Ed25519Math.pointDecompress(Rs);
            if (R == null) {
                return false;
            }

            byte[] sigR = new byte[32];
            System.arraycopy(signature.getSignature(), 32, sigR, 0, 32);

            BigInteger s = new BigInteger(1, Util.switchEndianness(sigR));
            BigInteger h = Ed25519Math.sha512_modq(Util.arrayConcat(
                    Util.arrayConcat(Rs, publicKey.getPublicKey()), message)
            );

            Point sB = Ed25519Math.pointMultiply(s, Ed25519Math.G);
            Point hA = Ed25519Math.pointMultiply(h, A);

            return Ed25519Math.pointEquals(sB, Ed25519Math.pointAdd(R, hA));
        } catch (Throwable e) {
            LOGGER.warn("Exception when verifying signature.", e);
            return false;
        }
    }

}
