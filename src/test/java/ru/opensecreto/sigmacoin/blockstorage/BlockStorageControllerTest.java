package ru.opensecreto.sigmacoin.blockstorage;

import org.testng.annotations.Test;
import ru.opensecreto.sigmacoin.config.EnvironmentConfig;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BlockStorageControllerTest {

    @Test
    public void testInvalidArguments() {
        BlockStorageController controller = new BlockStorageController(
                new File("testInvalid.db"), 4, 4,
                new BlockStorageConfiguration(EnvironmentConfig.BLOCK_STORAGE_DEFAULT_START_SIZE, EnvironmentConfig.BLOCK_STORAGE_DEFAULT_ALLOCATE_SIZE)
        );

        assertThatThrownBy(() ->
                controller.addBlock(new byte[3], new byte[4]))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->
                controller.addBlock(new byte[4], new byte[5]))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
                controller.getBlock(new byte[3]))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
                controller.hasBlock(new byte[3]))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void test() {
        BlockStorageController controller = new BlockStorageController(
                new File("test.db"), 2, 2,
                new BlockStorageConfiguration(EnvironmentConfig.BLOCK_STORAGE_DEFAULT_START_SIZE, EnvironmentConfig.BLOCK_STORAGE_DEFAULT_ALLOCATE_SIZE)
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
        BlockStorageController controllerNew = new BlockStorageController(
                new File("test.db"), 2, 2,
                new BlockStorageConfiguration(EnvironmentConfig.BLOCK_STORAGE_DEFAULT_START_SIZE, EnvironmentConfig.BLOCK_STORAGE_DEFAULT_ALLOCATE_SIZE)
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
