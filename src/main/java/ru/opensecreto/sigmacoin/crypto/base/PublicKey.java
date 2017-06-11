package ru.opensecreto.sigmacoin.crypto.base;

import com.google.common.primitives.Ints;
import ru.opensecreto.openutil.Util;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class PublicKey {

    private final byte[] publicKey;
    private final int method;

    public PublicKey(byte[] data) throws NullPointerException {
        checkNotNull(data);
        checkArgument(data.length >= 4);
        this.method = Ints.fromByteArray(data);
        this.publicKey = Arrays.copyOfRange(data, 4, data.length);
    }

    public PublicKey(int method, byte[] publicKey) throws NullPointerException {
        this.publicKey = Arrays.copyOf(checkNotNull(publicKey), publicKey.length);
        this.method = method;
    }

    public byte[] getPublicKey() {
        return Arrays.copyOf(publicKey, publicKey.length);
    }

    public int getMethod() {
        return method;
    }

    public byte[] encode() {
        return Util.arrayConcat(Ints.toByteArray(method), publicKey);
    }
}
