package ru.opensecreto.j2p;

import java.net.Socket;

public interface ConnectionHandler {

    public void handle(Socket socket, Controller controller);

}
