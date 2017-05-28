package ru.opensecreto.sigmacoin.vm;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class UtilTest {

    @Test
    public void testShift() {
        byte[] data1 = new byte[]{0b0000_0001, 0b0000_0001};
        Util.shiftLeft(data1, 2);
        Assertions.assertThat(data1).containsExactly(new byte[]{0b0000_0100, 0b0000_0100});

        byte[] data2 = new byte[]{0b0000_0001, 0b0000_0001};
        Util.shiftLeft(data2, 8);
        Assertions.assertThat(data1).containsExactly(new byte[]{0b0000_0001, 0b0000_0000});

        byte[] data3 = new byte[]{0b0000_0001, 0b0000_0001};
        Util.shiftLeft(data3, 9);
        Assertions.assertThat(data1).containsExactly(new byte[]{0b0000_0010, 0b0000_0000});
    }
}
