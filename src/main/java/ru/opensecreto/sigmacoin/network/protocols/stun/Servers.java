package ru.opensecreto.sigmacoin.network.protocols.stun;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Stun servers list collected from public sources,
 */
public class Servers {

    private static final Logger LOGGER = LoggerFactory.getLogger(Servers.class);

    public static final List<InetSocketAddress> servers = Collections.unmodifiableList(new ArrayList<InetSocketAddress>() {{
        Scanner in = null;
        try {
            in = new Scanner(getClass().getClassLoader().getResourceAsStream("stun-servers.txt"));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] data = line.split(":");
                try {
                    add(new InetSocketAddress(data[0], Integer.parseInt(data[1])));
                } catch (NumberFormatException e) {
                    LOGGER.warn("Error while parsing stun servers. \"{}\"", line, e);
                }
            }
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
                LOGGER.warn("Error while closing file.", e);
            }
        }
    }});

}
