package ru.opensecreto.sigmacoin;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import javax.xml.bind.DatatypeConverter;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilTest {

    @Test
    public void testShiftLeft() {
        byte[] data1 = new byte[]{0b0000_0001, 0b0000_0001};
        ru.opensecreto.sigmacoin.Util.shiftLeft(data1, 2);
        Assertions.assertThat(data1).inBinary().containsExactly(new byte[]{0b0000_0100, 0b0000_0100});

        byte[] data2 = new byte[]{0b0000_0001, 0b0000_0001};
        ru.opensecreto.sigmacoin.Util.shiftLeft(data2, 8);
        Assertions.assertThat(data2).inBinary().containsExactly(new byte[]{0b0000_0001, 0b0000_0000});

        byte[] data3 = new byte[]{0b0000_0001, 0b0000_0001};
        ru.opensecreto.sigmacoin.Util.shiftLeft(data3, 9);
        Assertions.assertThat(data3).inBinary().containsExactly(new byte[]{0b0000_0010, 0b0000_0000});
    }

    @Test
    public void testShiftRight() {
        byte[] data1 = new byte[]{0b0001_0000, 0b0001_0000};
        ru.opensecreto.sigmacoin.Util.shiftRight(data1, 2);
        Assertions.assertThat(data1).inBinary().containsExactly(new byte[]{0b0000_0100, 0b0000_0100});

        byte[] data2 = new byte[]{0b0000_0001, 0b0000_0001};
        ru.opensecreto.sigmacoin.Util.shiftRight(data2, 8);
        Assertions.assertThat(data2).inBinary().containsExactly(new byte[]{0b0000_0000, 0b0000_0001});

        byte[] data3 = new byte[]{0b0001_0000, 0b0001_0000};
        ru.opensecreto.sigmacoin.Util.shiftRight(data3, 9);
        Assertions.assertThat(data3).inBinary().containsExactly(new byte[]{0b0000_0000, 0b0000_1000});
    }

    @Test
    public void testBigToLittleEndianConversion() {
        assertThat(Util.switchEndianness(
                DatatypeConverter.parseHexBinary("a1b2c3d4")
        )).inHexadecimal().containsExactly(
                DatatypeConverter.parseHexBinary("d4c3b2a1")
        );
    }

    @Test
    public void testArrayConcat() {
        assertThat(Util.arrayConcat(
                DatatypeConverter.parseHexBinary("1234"),
                DatatypeConverter.parseHexBinary("5678")
        )).inHexadecimal().containsExactly(
                DatatypeConverter.parseHexBinary("12345678")
        );
    }
}
