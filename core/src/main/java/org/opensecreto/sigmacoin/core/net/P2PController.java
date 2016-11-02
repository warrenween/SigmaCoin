package org.opensecreto.sigmacoin.core.net;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class P2PController {

    private RandomAccessFile peers;

    /**
     * Initialises {@link P2PController}: loads peers
     * list etc.
     *
     * @param dataFolder folder where all app data is stored. Folder guaranteed to exist.
     *                   Must not be modified!
     */
    public P2PController(File dataFolder) throws IOException {

    }

}
