package socket;

import dispatcher.BinaryResponser;
import dispatcher.Responser;
import dispatcher.TextResponser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.NoSuchFileException;

import static dispatcher.Responser.*;
import static util.Constant.Symbols.SPACE;
import static util.FileUtils.*;

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
                try {
                    File currentFile = getFileFromRequest(connection);
                    String contentType = getFileContentType(currentFile);
                    if (isBinaryFileByContentType(contentType)) {
                        writeResponse(connection, getFileContentBinary(currentFile), contentType);
                        return;
                    }
                    if (isTextFileByContentType(contentType)){
                        writeResponse(connection, getFileContentText(currentFile), contentType);
                        return;
                    }
                    throw new IllegalArgumentException();
                } catch (IllegalArgumentException e) {
                    writeErrorResponse(connection, BAD_REQUEST_STATUS);
                } catch (NoSuchFileException e) {
                    writeErrorResponse(connection, NOT_FOUND_STATUS);
                } catch (RuntimeException e) {
                    writeErrorResponse(connection, INTERNAL_SERVER_ERROR_STATUS);
                }
            }
        }
    }

    private static void writeErrorResponse(Socket connection, Integer status) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        writer.write(Responser.getErrorResponseByHttpStatus(status));
        writer.flush();
    }

    private File getFileFromRequest(Socket connection) throws IOException {
        var bufferedInputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = bufferedInputStream.readLine();
        if (line == null || !line.contains(SPACE)) {
            throw new IllegalArgumentException();
        }
        return new File(pathToResources.concat(line.split(SPACE)[1]));
    }

    private void writeResponse(Socket connection, String content, String contentType) throws IOException {
        var writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        writer.write(textResponser.getResponse(content, contentType));
        writer.flush();
    }

    private void writeResponse(Socket connection, byte[] content, String contentType) throws IOException {
        var binaryOutputStream = connection.getOutputStream();
        binaryOutputStream.write(binaryResponser.getResponse(content, contentType));
        binaryOutputStream.flush();
    }
}
