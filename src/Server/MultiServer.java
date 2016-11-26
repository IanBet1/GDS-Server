package Server;

import java.net.*;
import java.io.*;

public class MultiServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(1094);
            System.err.println("Servidor GDS v1.0.0 inicializado na porta 1094.\nAguardando novas conexões.");
        } catch (IOException e) {
            System.err.println("Impossível escutar na porta 1094.");
            System.exit(-1);
        }

        while (listening) {
            new MultiServerThread(serverSocket.accept()).start();
        }

        serverSocket.close();
    }
}
