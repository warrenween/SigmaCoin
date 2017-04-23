package ru.opensecreto.sigmacoin.time;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TimeSyncronizer {

    private final static Logger LOGGER = LoggerFactory.getLogger(TimeSyncronizer.class);

    public static final List<InetAddress> servers = Collections.unmodifiableList(new ArrayList<InetAddress>() {{
        Scanner in = null;
        try {
            in = new Scanner(
                    TimeSyncronizer.class.getClassLoader().getResourceAsStream("ntpservers.txt")
            );
            while (in.hasNextLine()) {
                String line = in.nextLine();
                try {
                    add(InetAddress.getByName(line));
                } catch (UnknownHostException e) {
                    LOGGER.warn("Could not parse '{}'.", line, e);
                }
            }
        } catch (Throwable e) {
            LOGGER.warn("Error while parsing NTP servers list.", e);
        } finally {
            try {
                if (in != null) in.close();
            } catch (Throwable e) {
                LOGGER.error("Error while closing scanner.", e);
            }
        }
    }});

    private final Time time;
    private final NTPUDPClient ntpClient = new NTPUDPClient();

    public TimeSyncronizer(Time time) {
        this.time = time;
    }
}
