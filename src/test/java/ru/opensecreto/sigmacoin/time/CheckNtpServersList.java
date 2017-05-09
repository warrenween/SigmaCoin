package ru.opensecreto.sigmacoin.time;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CheckNtpServersList {

    public static final Logger LOGGER = LoggerFactory.getLogger(CheckNtpServersList.class);

    @Test
    public void checkNtpServers() {
        TimeSyncronizer syncronizer = new TimeSyncronizer(new Time());
        TimeSyncronizer.servers.forEach(address -> {
            try {
                syncronizer.update(address);
            } catch (Exception e) {
                LOGGER.error("Error while synchronizing time.", e);
            }
        });
    }
}
