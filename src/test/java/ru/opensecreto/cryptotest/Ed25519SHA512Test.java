package ru.opensecreto.cryptotest;

import org.testng.annotations.Test;
import ru.opensecreto.crypto.Ed25519SHA512;

import javax.xml.bind.DatatypeConverter;

import static org.assertj.core.api.Assertions.assertThat;

public class Ed25519SHA512Test {

    @Test
    public void testPubKeyGeneration() {
        assertThat(
                Ed25519SHA512.getPublicKey(
                        DatatypeConverter.parseHexBinary("9d61b19deffd5a60ba844af492ec2cc44449c5697b326919703bac031cae7f60")))
                .inHexadecimal()
                .containsExactly(
                        DatatypeConverter.parseHexBinary("d75a980182b10ab7d54bfed3c964073a0ee172f3daa62325af021a68f707511a")
                );

    }

}
