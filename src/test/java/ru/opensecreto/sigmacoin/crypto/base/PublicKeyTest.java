package ru.opensecreto.sigmacoin.crypto.base;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PublicKeyTest {

    @Test
    public void testCreationFromArray() {
        PublicKey publicKey = new PublicKey(new byte[]{0x00, 0x12, 0x34, 0x56, 0x76, 0x54, 0x32, 0x11, 0x7f});
        assertThat(publicKey.getMethod()).isEqualTo(0x00123456);
        assertThat(publicKey.getPublicKey()).containsExactly(new byte[]{
                0x76, 0x54, 0x32, 0x11, 0x7f
        });
    }

    @Test
    public void testUnmodifiable() {
        byte[] original = new byte[]{1, 2, 3, 4, 5, 6};
        PublicKey publicKey = new PublicKey(10, original);
        original[1] = 10;
        assertThat(publicKey.getPublicKey()).containsExactly(new byte[]{1, 2, 3, 4, 5, 6});
        byte[] data = publicKey.getPublicKey();
        data[0] = 100;
        assertThat(publicKey.getPublicKey()).containsExactly(new byte[]{1, 2, 3, 4, 5, 6});
    }

    @Test
    public void testEqualsTrue() {
        PublicKey publicKey1 = new PublicKey(10, new byte[]{1, 2, 3, 4, 5});
        PublicKey publicKey2 = new PublicKey(10, new byte[]{1, 2, 3, 4, 5});
        assertThat(publicKey1.equals(publicKey2)).isTrue();
    }

    @Test
    public void testEqualFalse() {
        PublicKey publicKey = new PublicKey(10, new byte[]{1, 2, 3, 4, 5});
        assertThat(publicKey.equals(null)).isFalse();
        assertThat(publicKey.equals("abc")).isFalse();
        assertThat(publicKey.equals(new PublicKey(55, new byte[]{1, 2, 3, 4, 5})));
        assertThat(publicKey.equals(new PublicKey(10, new byte[]{5, 4, 3, 2, 1})));
    }

}
