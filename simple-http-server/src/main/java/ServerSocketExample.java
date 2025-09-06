

import socket.CustomSocket;

import java.io.*;
import java.net.ServerSocket;

import static util.Constant.*;

public class ServerSocketExample {

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            System.out.printf("Server started at http://localhost:%s\n", DEFAULT_PORT);

            CustomSocket customSocket = new CustomSocket();
            customSocket.getConnection(serverSocket);
        }
    }
}
