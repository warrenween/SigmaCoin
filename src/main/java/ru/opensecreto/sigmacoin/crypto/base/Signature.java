package ru.opensecreto.sigmacoin.crypto.base;

import com.google.common.primitives.Ints;
import ru.opensecreto.openutil.Util;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

public class Signature {

    private final int method;
    private final byte[] signature;

    public Signature(int method, byte[] signature) {
        this.method = method;
        this.signature = Arrays.copyOf(checkNotNull(signature), signature.length);
    }

    public int getMethod() {
        return method;
    }

    public byte[] getSignature() {
        return Arrays.copyOf(signature, signature.length);
    }

    public byte[] encode() {
        return Util.arrayConcat(Ints.toByteArray(method), signature);
    }

    ;

}
