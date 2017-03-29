package ru.opensecreto.sigmacoin.network.commandhandlers;

import ru.opensecreto.sigmacoin.network.CommandHandler;
import ru.opensecreto.sigmacoin.network.Controller;

import java.io.*;
import java.net.Socket;

public class ConnectionInterruptHandler implements CommandHandler {

    @Override
    public void handle(Socket socket, DataInputStream in, DataOutputStream out, Controller controller) throws IOException {
        socket.close();
    }
}
