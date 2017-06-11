package ru.opensecreto.sigmacoin.crypto.base;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SignatureTest {

    @Test
    public void testCreationFromArray() {
        Signature signature = new Signature(new byte[]{0x00, 0x12, 0x34, 0x56, 0x76, 0x54, 0x32, 0x11, 0x7f});
        assertThat(signature.getMethod()).isEqualTo(0x00123456);
        assertThat(signature.getSignature()).containsExactly(new byte[]{
                0x76, 0x54, 0x32, 0x11, 0x7f
        });
    }

    @Test
    public void testUnmodifiable() {
        byte[] original = new byte[]{1, 2, 3, 4, 5, 6};
        Signature signature = new Signature(10, original);
        original[1] = 10;
        assertThat(signature.getSignature()).containsExactly(new byte[]{1, 2, 3, 4, 5, 6});
        byte[] data = signature.getSignature();
        data[0] = 100;
        assertThat(signature.getSignature()).containsExactly(new byte[]{1, 2, 3, 4, 5, 6});
    }

}
