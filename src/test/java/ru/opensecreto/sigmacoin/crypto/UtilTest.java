package ru.opensecreto.sigmacoin.crypto;

import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.Util;

import javax.xml.bind.DatatypeConverter;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilTest {

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
