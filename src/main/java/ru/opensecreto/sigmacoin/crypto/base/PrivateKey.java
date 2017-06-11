package ru.opensecreto.sigmacoin.crypto.base;

import com.google.common.primitives.Ints;
import ru.opensecreto.openutil.Util;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class PrivateKey {

    private final byte[] privateKey;
    private final int method;

    public PrivateKey(byte[] data) throws NullPointerException {
        checkNotNull(data);
        checkArgument(data.length >= 4);
        this.method = Ints.fromByteArray(data);
        this.privateKey = Arrays.copyOfRange(data, 4, data.length);
    }

    public PrivateKey(int method, byte[] privateKey) throws NullPointerException {
        this.privateKey = Arrays.copyOf(checkNotNull(privateKey), privateKey.length);
        this.method = method;
    }

    public byte[] getPrivateKey() {
        return Arrays.copyOf(privateKey, privateKey.length);
    }

    public int getMethod() {
        return method;
    }

    public byte[] encode() {
        return Util.arrayConcat(Ints.toByteArray(method), privateKey);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof PrivateKey) &&
                (((PrivateKey) obj).method == method) && Arrays.equals(((PrivateKey) obj).privateKey, privateKey);
    }
}
