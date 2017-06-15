package ru.opensecreto.sigmacoin.blockchain;

import ru.opensecreto.sigmacoin.blockchaintorage.BlockchainStorageController;

public class BlockchainController {

    private final BlockchainStorageController storageController;

    public BlockchainController(Block genesis, BlockchainStorageController storageController) {
        this.storageController = storageController;
    }
}
