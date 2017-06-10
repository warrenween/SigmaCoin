package ru.opensecreto.sigmacoin.crypto.base;

import com.google.common.primitives.Ints;
import ru.opensecreto.openutil.Util;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Signature {

    private final int method;
    private final byte[] signature;

    public Signature(byte[] data) throws NullPointerException, IllegalArgumentException {
        checkNotNull(data);
        checkArgument(data.length >= 4);
        this.method = Ints.fromByteArray(data);
        this.signature = Arrays.copyOfRange(data, 4, data.length);
    }

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

}
