package ru.opensecreto.j2p;

import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final Controller controller;

    public ConnectionHandler(Socket socket, Controller controller) {
        this.socket = socket;
        this.controller = controller;
    }

    @Override
    public void run() {

    }
}
