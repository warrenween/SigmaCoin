package org.opensecreto.thegreatearchive;

import java.io.IOException;
import java.io.RandomAccessFile;

public class DatabaseController {

    public DatabaseConfiguration configuration;
    private RandomAccessFile database;

    public DatabaseController(DatabaseConfiguration configuration) throws IOException {
        this.configuration = configuration;
        configuration.setImmutable();

        database = new RandomAccessFile(configuration.getPath(), "rwd");

    }

    /**
     * @param key
     * @return String data if found or null if not found.
     * @throws IOException
     */
    public String get(String key) throws IOException {
        database.seek(0);
        while (database.getFilePointer() < database.length()) {
            String index = database.readLine();
            String data = database.readLine();

            if (index.equals(key)) {
                return data;
            }
        }
        return null;
    }

    public void put(String key, String data) {

    }

}
