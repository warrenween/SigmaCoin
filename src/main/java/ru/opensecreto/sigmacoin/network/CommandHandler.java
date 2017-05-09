package ru.opensecreto.sigmacoin.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public interface CommandHandler {

    public void handle(Socket socket, DataInputStream in, DataOutputStream out, Controller controller) throws IOException;

}
