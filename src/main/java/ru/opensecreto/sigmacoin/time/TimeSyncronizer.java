package ru.opensecreto.sigmacoin.time;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeSyncronizer {

    private final static Logger LOGGER = LoggerFactory.getLogger(TimeSyncronizer.class);
    private final static int defaultNTPport = 123;
    private final static int NTPport;

    private final int port;
    private final Time time;

    static {
        int tempPort;
        if (System.getProperty("time.ntpport") == null) {
            LOGGER.info("System property 'time.ntpport' is not defined. Using default {}.", defaultNTPport);
            tempPort = defaultNTPport;
        } else {
            try {
                tempPort = Integer.parseInt(System.getProperty("time.ntpport"));
            } catch (NumberFormatException e) {
                LOGGER.warn("Could not parse property 'time.ntpport'", e);
                tempPort = defaultNTPport;
            }
        }
        NTPport = tempPort;
    }

    public TimeSyncronizer(int port, Time time) {
        this.port = port;
        this.time = time;
    }
}
