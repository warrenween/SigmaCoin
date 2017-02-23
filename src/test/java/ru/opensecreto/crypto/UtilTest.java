package ru.opensecreto.crypto;

import org.testng.annotations.Test;

import javax.xml.bind.DatatypeConverter;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilTest {

    @Test
    public void testBigToLittleEndianConversion() {
        assertThat(Util.bigToLittleEndian(new byte[]{(byte) 0xA1, (byte) 0xB2, (byte) 0xC3, (byte) 0xD4})).inHexadecimal()
                .containsExactly((byte) 0xD4, (byte) 0xC3, (byte) 0xB2, (byte) 0xA1);
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
