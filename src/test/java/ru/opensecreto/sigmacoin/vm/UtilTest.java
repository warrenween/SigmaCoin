package ru.opensecreto.sigmacoin.vm;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

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
}
