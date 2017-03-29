package ru.opensecreto.sigmacoin.network;

import java.io.*;
import java.net.Socket;

public interface CommandHandler {

    public void handle(Socket socket, DataInputStream in, DataOutputStream out, Controller controller) throws IOException;

}
