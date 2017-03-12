package ru.opensecreto.j2p;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WelcomeRunnable implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(WelcomeRunnable.class);
    private final int welcomePort;
    private final Controller controller;
    private final ConnectionHandler handler;

    private InetAddress bindAddress;
    private ServerSocket serverSocket;

    public WelcomeRunnable(int welcomePort, Controller controller, ConnectionHandler handler) {
        this.welcomePort = welcomePort;
        this.controller = controller;
        this.handler = handler;
    }

    private void portForward() {
        try {
            GatewayDiscover gatewayDiscover;
            GatewayDevice device;
            gatewayDiscover = new GatewayDiscover();
            gatewayDiscover.discover();
            device = gatewayDiscover.getValidGateway();
            if (device == null) {
                LOGGER.warn("No valid gateway device found.");
            } else {
                LOGGER.debug("External IP: " + device.getExternalIPAddress());
                controller.updateExternalAddress(InetAddress.getByName(device.getExternalIPAddress()));

                LOGGER.debug("Attempting to UPnP port {}.", Controller.DEFAULT_PORT);
                PortMappingEntry portMapping = new PortMappingEntry();

                LOGGER.debug("Querying device to see if mapping for port {} already exists",
                        Controller.DEFAULT_PORT);

                if (!device.getSpecificPortMappingEntry(Controller.DEFAULT_PORT, "TCP", portMapping)) {
                    LOGGER.debug("Port was already mapped.");
                    bindAddress = device.getLocalAddress();
                } else {
                    LOGGER.debug("Sending port mapping request");
                    if (!device.addPortMapping(Controller.DEFAULT_PORT, Controller.DEFAULT_PORT,
                            device.getLocalAddress().getHostAddress(), "TCP", "welcome")) {
                        LOGGER.warn("Port mapping attempt failed");
                    } else {
                        LOGGER.info("Mapping successful.");
                        bindAddress = device.getLocalAddress();
                    }
                }
            }
        } catch (Throwable e) {
            LOGGER.warn("Error while forwarding.", e);
        }
    }

    private void setupServerSocket() {
        try {
            if (bindAddress == null) {
                LOGGER.debug("Creating ServerSocket.");
                serverSocket = new ServerSocket(Controller.DEFAULT_PORT, 10);
            } else {
                serverSocket = new ServerSocket(Controller.DEFAULT_PORT, 10, bindAddress);
                LOGGER.debug("Creating ServerSocket.");
            }
        } catch (IOException e) {
            LOGGER.error("Could not set up ServerSocket. Accepting incoming connection is not possible.", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        portForward();
        setupServerSocket();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                LOGGER.debug("Accepted connection from {}.", socket.getInetAddress());
                handler.handle(socket, controller);
            } catch (IOException e) {
                LOGGER.error("Exception was thrown while waiting for connections.", e);
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

}
