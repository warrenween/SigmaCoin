package ru.opensecreto.cryptotest;

import org.testng.annotations.Test;
import ru.opensecreto.crypto.Util;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilTest {

    @Test
    public void testBigToLittleEndianConversion() {
        assertThat(Util.bigToLittleEndian(new byte[]{(byte) 0xA1, (byte) 0xB2, (byte) 0xC3, (byte) 0xD4})).inHexadecimal()
                .containsExactly((byte) 0xD4, (byte) 0xC3, (byte) 0xB2, (byte) 0xA1);
    }


}
