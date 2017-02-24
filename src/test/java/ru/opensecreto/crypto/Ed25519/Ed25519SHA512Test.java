package ru.opensecreto.crypto.Ed25519;

import org.testng.annotations.Test;

import javax.xml.bind.DatatypeConverter;

import static org.assertj.core.api.Assertions.assertThat;

public class Ed25519SHA512Test {

    @Test
    public void testPubKeyGeneration() {
        assertThat(
                Ed25519SHA512.getPublicKey(
                        DatatypeConverter.parseHexBinary("9d61b19deffd5a60ba844af492ec2cc44449c5697b326919703bac031cae7f60")
                ))
                .inHexadecimal()
                .containsExactly(
                        DatatypeConverter.parseHexBinary("d75a980182b10ab7d54bfed3c964073a0ee172f3daa62325af021a68f707511a")
                );
        assertThat(
                Ed25519SHA512.getPublicKey(
                        DatatypeConverter.parseHexBinary("4ccd089b28ff96da9db6c346ec114e0f5b8a319f35aba624da8cf6ed4fb8a6fb")
                ))
                .inHexadecimal()
                .containsExactly(
                        DatatypeConverter.parseHexBinary("3d4017c3e843895a92b70aa74d1b7ebc9c982ccf2ec4968cc0cd55f12af4660c")
                );
        assertThat(
                Ed25519SHA512.getPublicKey(
                        DatatypeConverter.parseHexBinary("c5aa8df43f9f837bedb7442f31dcb7b166d38535076f094b85ce3a2e0b4458f7")
                ))
                .inHexadecimal()
                .containsExactly(
                        DatatypeConverter.parseHexBinary("fc51cd8e6218a1a38da47ed00230f0580816ed13ba3303ac5deb911548908025")
                );
        assertThat(
                Ed25519SHA512.getPublicKey(
                        DatatypeConverter.parseHexBinary("f5e5767cf153319517630f226876b86c8160cc583bc013744c6bf255f5cc0ee5")
                ))
                .inHexadecimal()
                .containsExactly(
                        DatatypeConverter.parseHexBinary("278117fc144c72340f67d0f2316e8386ceffbf2b2428c9c51fef7c597f1d426e")
                );
    }

    @Test
    public void testSigning() {
        assertThat(
                new Ed25519SHA512()
                        .sign(
                                new byte[0],
                                DatatypeConverter.parseHexBinary("9d61b19deffd5a60ba844af492ec2cc44449c5697b326919703bac031cae7f60")
                        )
        )
                .inHexadecimal()
                .containsExactly(
                        DatatypeConverter.parseHexBinary("e5564300c360ac729086e2cc806e828a84877f1eb8e5d974d873e065224901555fb8821590a33bacc61e39701cf9b46bd25bf5f0595bbe24655141438e7a100b")
                );
        assertThat(
                new Ed25519SHA512()
                        .sign(
                                DatatypeConverter.parseHexBinary("72"),
                                DatatypeConverter.parseHexBinary("4ccd089b28ff96da9db6c346ec114e0f5b8a319f35aba624da8cf6ed4fb8a6fb")
                        )
        )
                .inHexadecimal()
                .containsExactly(
                        DatatypeConverter.parseHexBinary("92a009a9f0d4cab8720e820b5f642540a2b27b5416503f8fb3762223ebdb69da085ac1e43e15996e458f3613d0f11d8c387b2eaeb4302aeeb00d291612bb0c00")
                );
    }


}
