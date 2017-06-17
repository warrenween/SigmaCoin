package ru.opensecreto.sigmacoin.blockchaintorage;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.blockchain.Block;
import ru.opensecreto.sigmacoin.blockchain.FullBlockHeader;
import ru.opensecreto.sigmacoin.blockchain.RawBlockHeader;
import ru.opensecreto.sigmacoin.blockchain.Transaction;
import ru.opensecreto.sigmacoin.config.BaseConfig;
import ru.opensecreto.sigmacoin.crypto.base.Signature;
import ru.opensecreto.sigmacoin.vm.AccountAddress;
import ru.opensecreto.sigmacoin.vm.Word;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class BlockchainStorageControllerTest {

    @Test
    public void testBlocks() {
        Random rnd = new Random();
        BlockchainStorageController controller = new BlockchainStorageController(
                new File("testBlocks.db"), SHA3Digest::new
        );

        byte[] aA1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(aA1);
        byte[] aA2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(aA2);
        byte[] aA3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(aA3);
        byte[] aB1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(aB1);
        byte[] aB2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(aB2);
        byte[] aB3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(aB3);
        int[] powA = new int[4];
        for (int i = 0; i < powA.length; i++) {
            powA[i] = rnd.nextInt();
        }
        int[] powB = new int[4];
        for (int i = 0; i < powB.length; i++) {
            powB[i] = rnd.nextInt();
        }

        byte[] hashA1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hashA1);
        byte[] hashA2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hashA2);
        byte[] hashA3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hashA3);
        byte[] hashB1 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hashB1);
        byte[] hashB2 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hashB2);
        byte[] hashB3 = new byte[BaseConfig.DEFAULT_IDENTIFICATOR_LENGTH];
        rnd.nextBytes(hashB3);

        Block blockA = new Block(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                aA1, aA2, aA3
        ), powA), new ArrayList<byte[]>() {{
            add(hashA1);
            add(hashA2);
            add(hashA3);
        }});
        byte[] blockHashA = blockA.getBlockHash(SHA3Digest::new);
        Block blockB = new Block(new FullBlockHeader(new RawBlockHeader(
                BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3),
                aB1, aB2, aB3
        ), powA), new ArrayList<byte[]>() {{
            add(hashB1);
            add(hashB2);
            add(hashB3);
        }});
        byte[] blockHashB = blockB.getBlockHash(SHA3Digest::new);


        assertThat(controller.hasBlock(blockHashA)).isFalse();
        assertThat(controller.hasBlock(blockHashB)).isFalse();

        byte[] blockHashA1 = controller.addBlock(blockA);
        assertThat(blockHashA1).inHexadecimal().containsExactly(blockHashA);
        assertThat(controller.hasBlock(blockHashA));

        byte[] blockHashB1 = controller.addBlock(blockB);
        assertThat(blockHashB1).inHexadecimal().containsExactly(blockHashB);
        assertThat(controller.hasBlock(blockHashB));

        assertThat(controller.hasBlock(blockHashA)).isTrue();
        assertThat(controller.hasBlock(blockHashB)).isTrue();

        assertThat(controller.getBlock(blockHashA)).isEqualTo(blockA);
        assertThat(controller.getBlock(blockHashB)).isEqualTo(blockB);

        assertThat(controller.hasBlock(blockHashA)).isTrue();
        assertThat(controller.hasBlock(blockHashB)).isTrue();

        //test loading database
        controller.close();
        BlockchainStorageController controllerNew = new BlockchainStorageController(
                new File("testBlocks.db"), SHA3Digest::new
        );
        assertThat(controllerNew.hasBlock(blockHashA)).isTrue();
        assertThat(controllerNew.getBlock(blockHashA)).isEqualTo(blockA);
        assertThat(controllerNew.hasBlock(blockHashB)).isTrue();
        assertThat(controllerNew.getBlock(blockHashB)).isEqualTo(blockB);
    }

    @Test
    public void testTransactions() {
        BlockchainStorageController controller1 = new BlockchainStorageController(
                new File("txTest"), () -> new KeccakDigest(128)
        );
        Transaction transaction = new Transaction(
                BigInteger.valueOf(2),
                new AccountAddress(new Word(3)),
                BigInteger.valueOf(4),
                BigInteger.valueOf(5),
                new Signature(1, new byte[5]),
                new byte[20]
        );

        Digest digest = new KeccakDigest(128);
        byte[] txData1 = Transaction.encode(transaction);
        digest.update(txData1, 0, txData1.length);
        byte[] hash1 = new byte[digest.getDigestSize()];
        digest.doFinal(hash1, 0);

        assertThat(controller1.hasTransaction(hash1)).isFalse();
        byte[] hashController1 = controller1.addTransaction(transaction);
        assertThat(controller1.hasTransaction(hash1)).isTrue();
        assertThat(hashController1).containsExactly(hash1);

        Transaction transactionController1 = controller1.getTransaction(hashController1);
        assertThat(transaction).isEqualTo(transactionController1);

        controller1.close();
        BlockchainStorageController controller2 = new BlockchainStorageController(
                new File("txTest"), () -> new KeccakDigest(128)
        );
        assertThat(controller2.hasTransaction(hash1)).isTrue();

        Transaction transactionController2 = controller2.getTransaction(hashController1);
        assertThat(transactionController2).isEqualTo(transaction);
        controller2.close();
    }

}
