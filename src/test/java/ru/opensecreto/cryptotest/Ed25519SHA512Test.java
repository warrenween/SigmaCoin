package ru.opensecreto.cryptotest;

import org.assertj.core.api.Assertions;
import ru.opensecreto.crypto.Ed25519SHA512;

public class Ed25519SHA512Test {

    public void testPubKeyGeneration() {
        Assertions.assertThat(
                Ed25519SHA512.getPublicKey(new byte[]{
                        (byte) 0x9d, (byte) 0x61, (byte) 0xb1, (byte) 0x9d, (byte) 0xef, (byte) 0xfd, (byte) 0x5a, (byte) 0x60,
                        (byte) 0xba, (byte) 0x84, (byte) 0x4a, (byte) 0xf4, (byte) 0x92, (byte) 0xec, (byte) 0x2c, (byte) 0xc4,
                        (byte) 0x44, (byte) 0x49, (byte) 0xc5, (byte) 0x69, (byte) 0x7b, (byte) 0x32, (byte) 0x69, (byte) 0x19,
                        (byte) 0x70, (byte) 0x3b, (byte) 0xac, (byte) 0x03, (byte) 0x1c, (byte) 0xae, (byte) 0x7f, (byte) 0x60}))
                .containsExactly(
                        (byte) 0xd, (byte) 0x75, (byte) 0xa9, (byte) 0x80, (byte) 0x18, (byte) 0x2b, (byte) 0x10, (byte) 0xab7,
                        (byte) 0xd5, (byte) 0x4b, (byte) 0xfe, (byte) 0xd3, (byte) 0xc9, (byte) 0x64, (byte) 0x07, (byte) 0x3a,
                        (byte) 0x0e, (byte) 0xe1, (byte) 0x72, (byte) 0xf3, (byte) 0xda, (byte) 0xa6, (byte) 0x23, (byte) 0x25,
                        (byte) 0xaf, (byte) 0x02, (byte) 0x1a, (byte) 0x68, (byte) 0xf7, (byte) 0x07, (byte) 0x51, (byte) 0x1a
                );

    }

}
