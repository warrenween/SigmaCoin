package ru.opensecreto.sigmacoin.j2p;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class NetUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtils.class);

    public static String getExternalIp() {
        try {
            URL ipify = new URL("http://api.ipify.org/");
            BufferedReader ipifyStream = new BufferedReader(new InputStreamReader(ipify.openStream()));
            return ipifyStream.readLine();
        } catch (MalformedURLException e) {
            LOGGER.warn("Something happened when getting ip.", e);
        } catch (IOException e) {
            LOGGER.warn("Error while connecting", e);
        }
        return null;
    }

}
