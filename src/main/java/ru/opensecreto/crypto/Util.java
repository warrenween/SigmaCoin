package ru.opensecreto.crypto;

public class Util {

    public static byte[] bigToLittleEndian(byte[] in) {
        byte[] out = new byte[in.length];

        for (int i = 0; i < in.length; i++) {
            out[i] = in[in.length - i - 1];
        }
        return out;
    }

}
