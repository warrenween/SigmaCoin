package ru.opensecreto.sigmacoin.crypto.Ed25519;

import ru.opensecreto.openutil.Util;
import ru.opensecreto.sigmacoin.crypto.base.BaseSigner;
import ru.opensecreto.sigmacoin.crypto.base.PrivateKey;
import ru.opensecreto.sigmacoin.crypto.base.PublicKey;
import ru.opensecreto.sigmacoin.crypto.base.Signature;

import java.math.BigInteger;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * See: EdDSA and Ed25519 draft-josefsson-eddsa-ed25519-03
 * <p>
 * https://tools.ietf.org/html/draft-josefsson-eddsa-ed25519-03
 */
public class Ed25519SHA512 implements BaseSigner {

    public static final int SIGNER_ID = 0;

    public static final int SIGNATURE_SIZE = 64;

    @Override
    public Signature sign(byte[] message, PrivateKey privateKey)
            throws NullPointerException, IllegalArgumentException {
        checkNotNull(message);
        checkNotNull(privateKey);
        checkArgument(privateKey instanceof Ed25519PrivateKey,
                "Private key must have class " + Ed25519PrivateKey.class + ". Given " + privateKey.getClass() + "."
        );

        Secret tmp = Ed25519Math.secretExpand(((Ed25519PrivateKey) privateKey).getPrivateKey());
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
        return new Signature(SIGNER_ID, Util.arrayConcat(Rs, Arrays.copyOf(Util.switchEndianness(s.toByteArray()), 32)));
    }

    @Override
    public boolean verify(byte[] message, Signature signature, PublicKey publicKey)
            throws NullPointerException, IllegalArgumentException {
        checkNotNull(message);
        checkNotNull(signature);
        checkNotNull(publicKey);

        checkArgument(publicKey instanceof Ed25519PublicKey,
                "Public key must have class " + Ed25519PublicKey.class + ". Given " + publicKey.getClass() + "."
        );
        checkArgument(signature.getMethod() == SIGNER_ID);
        checkArgument(signature.getSignature().length == SIGNATURE_SIZE);

        Ed25519PublicKey publicKeyCasted = (Ed25519PublicKey) publicKey;

        Point A = Ed25519Math.pointDecompress(publicKeyCasted.getPublicKey());
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
                Util.arrayConcat(Rs, publicKeyCasted.getPublicKey()), message)
        );

        Point sB = Ed25519Math.pointMultiply(s, Ed25519Math.G);
        Point hA = Ed25519Math.pointMultiply(h, A);

        return Ed25519Math.pointEquals(sB, Ed25519Math.pointAdd(R, hA));
    }

}
