package ru.opensecreto.j2p;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface CommandHandler {

    public void handle(Socket socket, InputStream in, OutputStream out, Controller controller) throws IOException;

}
