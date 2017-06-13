package ru.opensecreto.sigmacoin.blockchaintorage;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.blockchain.Transaction;
import ru.opensecreto.sigmacoin.crypto.base.Signature;
import ru.opensecreto.sigmacoin.vm.AccountAddress;
import ru.opensecreto.sigmacoin.vm.Word;

import java.io.File;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BlockchainStorageControllerTest {

    @Test
    public void testInvalidArguments() {
        BlockchainStorageController controller = new BlockchainStorageController(
                new File("testInvalid.db"), 4, SHA3Digest::new
        );

        assertThatThrownBy(() -> controller.addBlock(new byte[3], new byte[4]))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> controller.getBlock(new byte[3]))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> controller.hasBlock(new byte[3]))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testBlocks() {
        BlockchainStorageController controller = new BlockchainStorageController(
                new File("test.db"), 2, SHA3Digest::new
        );

        assertThat(controller.hasBlock(new byte[]{1, 2})).isFalse();

        assertThat(controller.addBlock(new byte[]{1, 2}, new byte[]{3, 4})).isTrue();

        //check reading block do not changes it
        assertThat(controller.hasBlock(new byte[]{1, 2})).isTrue();
        assertThat(controller.getBlock(new byte[]{1, 2})).containsExactly(new byte[]{3, 4});
        assertThat(controller.hasBlock(new byte[]{1, 2})).isTrue();
        assertThat(controller.getBlock(new byte[]{1, 2})).containsExactly(new byte[]{3, 4});

        //check adding block with same hash rejected and original block is not changed
        assertThat(controller.addBlock(new byte[]{1, 2}, new byte[]{5, 6})).isFalse();
        assertThat(controller.hasBlock(new byte[]{1, 2})).isTrue();
        assertThat(controller.getBlock(new byte[]{1, 2})).containsExactly(new byte[]{3, 4});

        //testing loading database
        controller.close();
        BlockchainStorageController controllerNew = new BlockchainStorageController(
                new File("test.db"), 2, SHA3Digest::new
        );

        //check reading block do not changes it
        assertThat(controllerNew.hasBlock(new byte[]{1, 2})).isTrue();
        assertThat(controllerNew.getBlock(new byte[]{1, 2})).containsExactly(new byte[]{3, 4});
        assertThat(controllerNew.hasBlock(new byte[]{1, 2})).isTrue();
        assertThat(controllerNew.getBlock(new byte[]{1, 2})).containsExactly(new byte[]{3, 4});

        //check adding block with same hash rejected and original block is not changed
        assertThat(controllerNew.addBlock(new byte[]{1, 2}, new byte[]{5, 6})).isFalse();
        assertThat(controllerNew.hasBlock(new byte[]{1, 2})).isTrue();
        assertThat(controllerNew.getBlock(new byte[]{1, 2})).containsExactly(new byte[]{3, 4});
    }

    @Test
    public void testTransactions() {
        BlockchainStorageController controller1 = new BlockchainStorageController(
                new File("txTest"), 32, () -> new KeccakDigest(128)
        );
        Transaction transaction1 = new Transaction(
                BigInteger.valueOf(1),
                BigInteger.valueOf(2),
                new AccountAddress(new Word(3)),
                BigInteger.valueOf(4),
                BigInteger.valueOf(5),
                new Signature(1, new byte[5]),
                new byte[20]
        );

        Digest digest = new KeccakDigest(128);
        byte[] txData1 = Transaction.encode(transaction1);
        digest.update(txData1, 0, txData1.length);
        byte[] hash1 = new byte[digest.getDigestSize()];
        digest.doFinal(hash1, 0);

        assertThat(controller1.hasTransaction(hash1)).isFalse();
        byte[] hashController1 = controller1.addTransaction(transaction1);
        assertThat(controller1.hasTransaction(hash1)).isTrue();
        assertThat(hashController1).containsExactly(hash1);

        Transaction transactionController1 = controller1.getTransaction(hashController1);
        assertThat(transaction1).isEqualTo(transactionController1);

        controller1.close();
        BlockchainStorageController controller2 = new BlockchainStorageController(
                new File("txTest"), 32, () -> new KeccakDigest(128)
        );
        assertThat(controller2.hasTransaction(hash1)).isTrue();

        Transaction transactionController2 = controller2.getTransaction(hashController1);
        assertThat(transactionController2).isEqualTo(transaction1);
    }

}
