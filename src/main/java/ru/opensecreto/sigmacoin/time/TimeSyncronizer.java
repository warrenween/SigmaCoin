package ru.opensecreto.sigmacoin.time;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static com.google.common.base.Preconditions.checkNotNull;

public class TimeSyncronizer {

    private final static Logger LOGGER = LoggerFactory.getLogger(TimeSyncronizer.class);
    public static final int DEFAULT_TIMEOUT = 60000;

    public static final List<InetAddress> servers = new ArrayList<InetAddress>() {{
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
    }};

    private final Time time;
    private final NTPUDPClient ntpClient;

    public TimeSyncronizer(Time time) {
        this(time, DEFAULT_TIMEOUT);
    }

    public TimeSyncronizer(Time time, int timeout) {
        this.time = checkNotNull(time);

        ntpClient = new NTPUDPClient();
        ntpClient.setDefaultTimeout(timeout);
    }

    public void update() throws IOException {
        if (servers.size() == 0) {
            LOGGER.warn("Can not synchronize time - NTP servers list is empty.");
            return;
        }

        update(servers.get(new Random().nextInt(servers.size())));
    }

    public void update(InetAddress server) throws IOException, NullPointerException {
        TimeInfo timeInfo = ntpClient.getTime(checkNotNull(server));
        timeInfo.computeDetails();
        time.setOffset(timeInfo.getOffset());

        LOGGER.info("Synchronized time with {}. Delay: {}ms. Offset: {}ms. Messages: {}.",
                server.getHostAddress(), timeInfo.getDelay(), timeInfo.getOffset(), timeInfo.getComments());
    }

}
