

import socket.CustomSocket;

import java.io.IOException;
import java.net.ServerSocket;

import static util.Constant.DEFAULT_PORT;
import static util.Constant.PATH_STATIC_FOLDER;

public class ServerSocketExample {

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT)) {
            System.out.printf("Server started at http://localhost:%s\n", DEFAULT_PORT);

            CustomSocket customSocket = new CustomSocket(getPathOrDefault(args));
            customSocket.establishConnection(serverSocket);
        }
    }

    private static String getPathOrDefault(String[] args) {
        return args == null || args.length == 0 || args[0] == null
            ? PATH_STATIC_FOLDER
            : args[0];
    }
}
