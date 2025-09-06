package socket;

import dispatcher.BinaryResponser;
import dispatcher.TextResponser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.Objects;
import java.util.Scanner;

import static util.Constant.ContentType.BINARY_CONTENT_TYPE_SUPPORTED_LIST;
import static util.Constant.ContentType.NON_BINARY_CONTENT_TYPE_SUPPORTED_LIST;
import static util.Constant.PATH_STATIC_FOLDER;
import static util.Constant.Symbols.SHORT_YES;
import static util.Constant.Symbols.SPACE;
import static util.FileUtils.getFileContentType;

public class CustomSocket {
    private final String PATH_TO_RESOURCES;
    private final BinaryResponser binaryResponser;
    private final TextResponser textResponser;

    public CustomSocket() {
        this.binaryResponser = new BinaryResponser();
        this.textResponser = new TextResponser();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Do you want to use default path to resources? [Y/N]");
            String inputData = scanner.nextLine();
            if (SHORT_YES.equals(inputData)) {
                this.PATH_TO_RESOURCES = PATH_STATIC_FOLDER;
                return;
            }
            System.out.println("Input path to resources:"); // D:/PabloPackage/java_project/payment-service/simple-http-server/src/main/resources/static/customPath
            this.PATH_TO_RESOURCES = scanner.nextLine();
        }
    }

    public void getConnection(ServerSocket serverSocket) throws IOException {
        while (true) {
            try (Socket connection = serverSocket.accept()) {
                String fileName = getFileNameFromRequest(connection);
                writeResponse(connection, fileName);
            }
        }
    }

    private String getFileNameFromRequest(Socket connection) throws IOException {
        var bufferedInputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        String fileFullName = null;
        for (boolean isFirstLine = true;(line = bufferedInputStream.readLine()) != null && !line.isEmpty();) {
            System.out.println(line);
            if (isFirstLine) {
                isFirstLine = false;
                fileFullName = line.split(SPACE)[1];
            }
        }
        System.out.println();
        return fileFullName;
    }

    private void writeResponse(Socket connection, String fileFullName) throws IOException {
        var binaryOutputStream = connection.getOutputStream();
        var textWriter = new BufferedWriter(new OutputStreamWriter(binaryOutputStream));

        if (Objects.isNull(fileFullName) || fileFullName.isEmpty()) {
            sendResponseAndFlush(textWriter, textResponser.getErrorResponse404());
            return;
        }

        File currentFile = new File(PATH_TO_RESOURCES.concat(fileFullName));

        try {
            String contentType = getFileContentType(currentFile);
            if (BINARY_CONTENT_TYPE_SUPPORTED_LIST.contains(contentType)) {
                sendResponseAndFlush(binaryOutputStream, binaryResponser.getResponse(currentFile));
                return;
            }
            if (NON_BINARY_CONTENT_TYPE_SUPPORTED_LIST.contains(contentType)) {
                sendResponseAndFlush(textWriter, textResponser.getResponse(currentFile));
                return;
            }
            sendResponseAndFlush(textWriter, textResponser.getErrorResponse404());
        } catch (UnsupportedAddressTypeException e) {
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
