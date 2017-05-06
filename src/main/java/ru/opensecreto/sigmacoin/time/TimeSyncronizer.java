package ru.opensecreto.sigmacoin.time;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;

public class TimeSyncronizer {

    private final static Logger LOGGER = LoggerFactory.getLogger(TimeSyncronizer.class);

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
        this.time = time;

        ntpClient = new NTPUDPClient();
    }

    public void update() throws IOException {
        if (servers.size() == 0) {
            LOGGER.warn("Can not synchronize time - NTP servers list is empty.");
            return;
        }

        InetAddress server = servers.get(new Random().nextInt(servers.size()));

        TimeInfo timeInfo = ntpClient.getTime(server);
        timeInfo.computeDetails();
        time.setOffset(timeInfo.getOffset());

        LOGGER.info("Synchronized time with {}. Delay: {}ms. Offset: {}ms. Messages: {}.",
                server.getHostAddress(), timeInfo.getDelay(), timeInfo.getOffset(), timeInfo.getComments());
    }

}
