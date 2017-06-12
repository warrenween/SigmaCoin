package ru.opensecreto.sigmacoin.crypto.empty;

import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.crypto.base.PrivateKey;
import ru.opensecreto.sigmacoin.crypto.base.PublicKey;
import ru.opensecreto.sigmacoin.crypto.base.Signature;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmptySignerTest {

    @Test
    public void testNull() {
        EmptySigner emptySigner = new EmptySigner();

        assertThatThrownBy(() -> emptySigner.sign(
                null, new PrivateKey(0, new byte[0])
        )).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> emptySigner.sign(
                new byte[5], null
        )).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> emptySigner.verify(
                null,
                new Signature(1, new byte[]{}),
                new PublicKey(55, new byte[5])
        )).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> emptySigner.verify(
                new byte[5],
                null,
                new PublicKey(55, new byte[5])
        )).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> emptySigner.verify(
                new byte[5],
                new Signature(1, new byte[]{}),
                null
        )).isInstanceOf(NullPointerException.class);
    }

}
