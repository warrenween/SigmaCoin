package ru.opensecreto.sigmacoin.vm;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;
import org.jetbrains.annotations.NotNull;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * Word is 32 byte (256 bits) signed value. Most significant bit as at the begining of array.
 * Word is immutable. All operations return new word.
 */
public final class Word implements Comparable<Word> {

    public static final int WORD_SIZE = 32;

    public static final Word WORD_0 = new Word(0);
    public static final Word WORD_1 = new Word(1);
    public static final Word WORD_2 = new Word(2);

    private final byte[] data = new byte[WORD_SIZE];

    public Word() {
    }

    public Word(byte[] data) throws IllegalArgumentException {
        if (data.length != 32) throw new IllegalArgumentException("data length must be 32");
        System.arraycopy(data, 0, this.data, 0, WORD_SIZE);
    }

    public Word(byte value) {
        data[31] = value;
        if (value < 0) {
            for (int i = 0; i < data.length - 1; i++) {
                data[i] = (byte) 0xff;
            }
        }
    }

    public Word(short value) {
        System.arraycopy(Shorts.toByteArray(value), 0, data, 30, 2);
        if (value < 0) {
            for (int i = 0; i < data.length - 2; i++) {
                data[i] = (byte) 0xff;
            }
        }
    }

    public Word(int value) {
        System.arraycopy(Ints.toByteArray(value), 0, data, 28, 4);
        if (value < 0) {
            for (int i = 0; i < data.length - 4; i++) {
                data[i] = (byte) 0xff;
            }
        }
    }

    public boolean isInRange(Word down, Word up) {
        if (down.compareTo(up) > 0) throw new IllegalArgumentException("bottom bottom must be less than top border");
        return (this.compareTo(down) >= 0) && (this.compareTo(up) <= 0);
    }

    public int toInt() {
        return Ints.fromBytes(data[28], data[29], data[30], data[31]);
    }

    public byte[] getData() {
        return Arrays.copyOf(data, 32);
    }

    public Word sum(Word value) {
        byte[] result = new byte[32];
        int carry = 0;
        for (int i = WORD_SIZE - 1; i >= 0; i--) {
            int tmp = (data[i] & 0xff) + (value.data[i] & 0xff) + carry;
            result[i] = (byte) (tmp & 0xff);
            carry = (tmp & 0xff00) >> 8;
        }
        return new Word(result);
    }

    /**
     * Subtract value from this
     *
     * @param value value to subtract
     * @return
     */
    public Word subtract(Word value) {
        return sum(value.negate());
    }

    public Word negate() {
        byte[] buf = Arrays.copyOf(data, WORD_SIZE);
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) ((~(buf[i] & 0xff)) & 0xff);
        }
        return new Word(buf).sum(new Word(1));
    }

    public boolean isPositive() {
        return (data[0] & 0xff >> 7) == 0;
    }

    public boolean isNegative() {
        return (data[0] & 0xff >> 7) == 1;
    }

    public Word multiply(Word value) {
        byte[] result = new byte[WORD_SIZE];
        byte[] buf = new byte[WORD_SIZE];
        System.arraycopy(data, 0, buf, 0, WORD_SIZE);

        for (int i = 0; i < 256; i++) {
            if (((value.data[WORD_SIZE - 1 - i / 8] & 0xff) & (1 << (i % 8))) != 0) {
                int carry = 0;
                for (int j = WORD_SIZE - 1; j >= 0; j--) {
                    int tmp = (result[j] & 0xff) + (buf[j] & 0xff) + carry;
                    result[j] = (byte) (tmp & 0xff);
                    carry = (tmp & 0xff00) >> 8;
                }
            }

            int caret = 0;
            for (int j = buf.length - 1; j >= 0; j--) {
                int tmp = (buf[j] & 0xff) << 1;
                buf[j] = (byte) ((tmp & 0xff) + caret);
                caret = (tmp & 0xff00) >> 8;
            }
        }
        return new Word(result);
    }

    /**
     * Make dividend/divisor anr return quotient and remainder
     *
     * @return Tuple(quotient, remainder)
     */
    private static Tuple2<Word, Word> divide(Word dividend, Word divisor) {
        if (divisor.equals(WORD_0)) {
            throw new ArithmeticException("division by zero");
        }
        if (divisor.equals(WORD_1)) {
            return Tuple.tuple(dividend, WORD_0);
        }

        boolean dividendSign = true;
        boolean divisorSign = true;
        if (dividend.isNegative()) {
            dividend = dividend.negate();
            dividendSign = false;
        }
        if (divisor.isNegative()) {
            divisor = divisor.negate();
            divisorSign = false;
        }

        Word quotient = new Word(0);

        while (dividend.compareTo(divisor) >= 0) {
            dividend = dividend.subtract(divisor);
            quotient = quotient.sum(WORD_1);
        }

        if (!dividendSign) {
            dividend = dividend.negate();
        }
        if (dividendSign != divisorSign) {
            quotient = quotient.negate();
        }
        return Tuple.tuple(quotient, dividend);
    }

    public Word div(Word divisor) {
        return divide(this, divisor).v1;
    }

    public Word mod(Word divisor) {
        return divide(this, divisor).v2;
    }

    public Word shiftLeft(int count) {
        byte[] buf = Arrays.copyOf(data, WORD_SIZE);
        Util.shiftLeft(buf, count);
        return new Word(buf);
    }

    public Word shiftRight(int count) {
        byte[] buf = Arrays.copyOf(data, WORD_SIZE);
        Util.shiftRight(buf, count);
        return new Word(buf);
    }

    @Override
    public int compareTo(@NotNull Word o) {
        if (equals(o)) return 0;
        return (subtract(o).isPositive()) ? 1 : -1;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null) && (obj instanceof Word) && Arrays.equals(((Word) obj).data, data);
    }

    @Override
    public String toString() {
        return "0x" + DatatypeConverter.printHexBinary(data);
    }
}
