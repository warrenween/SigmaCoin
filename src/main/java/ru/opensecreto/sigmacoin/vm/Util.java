package ru.opensecreto.sigmacoin.vm;

public class Util {

    public static void shiftLeft(byte[] data, int count) {
        for (int i = count / 8; i > 0; i--) {
            shiftByteLeft(data);
        }
        int caret = 0;
        count %= 8;
        for (int i = data.length - 1; i >= 0; i--) {
            int tmp = (data[i] & 0xff) << count;
            data[i] = (byte) ((tmp & 0xff) + caret);
            caret = (tmp & 0xff00) >> 8;
        }
    }

    /**
     * Shift by one byte left.
     *
     * @param data array to shift
     */
    private static void shiftByteLeft(byte[] data) {
        byte caret = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            byte tmp = data[i];
            data[i] = caret;
            caret = tmp;
        }
    }

}
