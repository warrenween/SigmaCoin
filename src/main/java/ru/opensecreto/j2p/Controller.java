package ru.opensecreto.j2p;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    private final File databaseFile;
    private DB database;

    public Controller(File databaseFile) {
        this.databaseFile = databaseFile;
        openDatabase();
    }

    private void openDatabase() {
        database = DBMaker.fileDB(databaseFile)
                .closeOnJvmShutdown()
                .transactionEnable()
                .make();    }

    private void close() {
        database.close();
    }

}
