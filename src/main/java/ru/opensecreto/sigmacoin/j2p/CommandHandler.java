package ru.opensecreto.sigmacoin.j2p;

import java.io.*;
import java.net.Socket;

public interface CommandHandler {

    public void handle(Socket socket, DataInputStream in, DataOutputStream out, Controller controller) throws IOException;

}
