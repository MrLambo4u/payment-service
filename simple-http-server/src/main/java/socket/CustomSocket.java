package socket;

import dispatcher.BinaryResponser;
import dispatcher.TextResponser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.UnsupportedAddressTypeException;

import static util.Constant.ContentType.BINARY_CONTENT_TYPE_SUPPORTED_SET;
import static util.Constant.ContentType.NON_BINARY_CONTENT_TYPE_SUPPORTED_SET;
import static util.Constant.Symbols.SPACE;
import static util.FileUtils.getFileContentType;

public class CustomSocket {
    private final String pathToResources;
    private final BinaryResponser binaryResponser;
    private final TextResponser textResponser;

    public CustomSocket(String path) {
        this.binaryResponser = new BinaryResponser();
        this.textResponser = new TextResponser();
        this.pathToResources = path;
    }

    public void establishConnection(ServerSocket serverSocket) throws IOException {
        while (true) {
            try (Socket connection = serverSocket.accept()) {
                String fileName = getFileNameFromRequest(connection);
                writeResponse(connection, fileName);
            }
        }
    }

    private String getFileNameFromRequest(Socket connection) throws IOException {
        var bufferedInputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = bufferedInputStream.readLine();
        return line == null || line.isEmpty()
                ? null
                : line.split(SPACE)[1];
    }

    private void writeResponse(Socket connection, String fileFullName) throws IOException {
        var binaryOutputStream = connection.getOutputStream();
        var textWriter = new BufferedWriter(new OutputStreamWriter(binaryOutputStream));

        if (fileFullName == null || fileFullName.isEmpty()) {
            sendResponseAndFlush(textWriter, textResponser.getErrorResponse404());
            return;
        }

        File currentFile = new File(pathToResources.concat(fileFullName));

        try {
            String contentType = getFileContentType(currentFile);
            if (BINARY_CONTENT_TYPE_SUPPORTED_SET.contains(contentType)) {
                sendResponseAndFlush(binaryOutputStream, binaryResponser.getResponse(currentFile));
                return;
            }
            if (NON_BINARY_CONTENT_TYPE_SUPPORTED_SET.contains(contentType)) {
                sendResponseAndFlush(textWriter, textResponser.getResponse(currentFile));
                return;
            }
            sendResponseAndFlush(textWriter, textResponser.getErrorResponse404());
        } catch (RuntimeException e) {
            sendResponseAndFlush(textWriter, textResponser.getErrorResponse404());
        }

    }

    private void sendResponseAndFlush(BufferedWriter writer, String content) throws IOException {
        writer.write(content);
        writer.flush();
    }

    private void sendResponseAndFlush(OutputStream outputStream, byte[] content) throws IOException {
        outputStream.write(content);
        outputStream.flush();
    }


}
