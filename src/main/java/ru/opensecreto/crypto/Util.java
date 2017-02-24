package ru.opensecreto.crypto;

public class Util {

    public static byte[] bigToLittleEndian(byte[] in) {
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

    public static byte[] arrayLim(byte[] a, int l) {
        byte[] out = new byte[l];
        if (a.length > l) {
            System.arraycopy(a, 0, out, 0, l);
        } else {
            System.arraycopy(a, 0, out, 0, a.length);
        }
        return out;
    }

}
