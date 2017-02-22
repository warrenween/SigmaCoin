package ru.opensecreto.cryptotest;

import org.testng.annotations.Test;
import ru.opensecreto.crypto.Util;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilTest {

    @Test
    public void testBigToLittleEndianConversion() {
        assertThat(Util.bigToLittleEndian(new byte[]{0b00000000, 0b00000010})).inBinary().containsExactly(new byte[]{
                0b01000000, 0b00000000
        });
    }


}
