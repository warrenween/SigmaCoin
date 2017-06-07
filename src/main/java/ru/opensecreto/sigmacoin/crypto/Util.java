package ru.opensecreto.sigmacoin.crypto;

public class Util {

    public static byte[] switchEndianness(byte[] in) {
        byte[] out = new byte[in.length];

        for (int i = 0; i < in.length; i++) {
            out[i] = in[in.length - i - 1];
        }
        return out;
    }

    public static byte[] arrayConcat(byte[] a, byte[] b) {
        byte[] out = new byte[a.length + b.length];
        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(b, 0, out, a.length, b.length);
        return out;
    }

}
