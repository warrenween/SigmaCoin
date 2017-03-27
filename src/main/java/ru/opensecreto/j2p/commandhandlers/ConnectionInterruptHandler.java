package ru.opensecreto.j2p.commandhandlers;

import ru.opensecreto.j2p.CommandHandler;
import ru.opensecreto.j2p.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionInterruptHandler implements CommandHandler {

    @Override
    public void handle(Socket socket, InputStream in, OutputStream out, Controller controller) throws IOException {
        socket.close();
    }
}
