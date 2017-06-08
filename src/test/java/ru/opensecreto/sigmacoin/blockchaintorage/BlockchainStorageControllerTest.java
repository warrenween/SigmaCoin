package ru.opensecreto.sigmacoin.blockchaintorage;

import org.testng.annotations.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BlockchainStorageControllerTest {

    @Test
    public void testInvalidArguments() {
        BlockchainStorageController controller = new BlockchainStorageController(
                new File("testInvalid.db"), 4
        );

        assertThatThrownBy(() -> controller.addBlock(new byte[3], new byte[4]))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> controller.getBlock(new byte[3]))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> controller.hasBlock(new byte[3]))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void test() {
        BlockchainStorageController controller = new BlockchainStorageController(
                new File("test.db"), 2
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
                new File("test.db"), 2
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

}
