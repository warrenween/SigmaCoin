package ru.opensecreto.sigmacoin.blockchain;

import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.crypto.base.Signature;
import ru.opensecreto.sigmacoin.vm.AccountAddress;
import ru.opensecreto.sigmacoin.vm.Word;

import java.math.BigInteger;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionTest {

    @Test
    public void testSerialization() {
        byte[] sig = new byte[64];
        new Random().nextBytes(sig);
        byte[] data = new byte[128];
        new Random().nextBytes(data);

        Transaction tx1 = new Transaction(
                BigInteger.valueOf(50),
                new AccountAddress(new Word(1256)),
                BigInteger.valueOf(100),
                BigInteger.valueOf(500),
                new Signature(10, sig),
                data
        );

        Transaction tx2 = Transaction.decode(Transaction.encode(tx1));

        assertThat(tx2.equals(tx1)).isTrue();
    }

    @Test
    public void testEquals() {
        byte[] sig = new byte[64];
        new Random().nextBytes(sig);
        byte[] data = new byte[128];
        new Random().nextBytes(data);

        Transaction tx1 = new Transaction(
                BigInteger.valueOf(50),
                new AccountAddress(new Word(1256)),
                BigInteger.valueOf(100),
                BigInteger.valueOf(500),
                new Signature(11, sig),
                data
        );

        Transaction tx2 = new Transaction(
                BigInteger.valueOf(50),
                new AccountAddress(new Word(1256)),
                BigInteger.valueOf(100),
                BigInteger.valueOf(500),
                new Signature(11, sig),
                data
        );

        assertThat(tx1.equals(tx2)).isTrue();
    }
}
