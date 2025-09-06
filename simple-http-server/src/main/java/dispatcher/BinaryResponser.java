package dispatcher;

import java.io.File;
import java.io.IOException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.charset.StandardCharsets;

import static util.Constant.HTTP_404_ERROR_RESPONSE;
import static util.FileUtils.getFileContentBinary;
import static util.FileUtils.getFileContentType;

public class BinaryResponser implements Responser<byte[]>{
    private static final byte[] ERROR_RESPONSE_404 = HTTP_404_ERROR_RESPONSE.getBytes();

    public byte[] getResponse(File file) {
        try {
            return file.exists()
                    ? getSuccessResponse(file)
                    : ERROR_RESPONSE_404;
        } catch (UnsupportedAddressTypeException | IOException e) {
            return ERROR_RESPONSE_404;
        }
    }

    private byte[] getSuccessResponse(File file) throws IOException {
        byte[] fileContent = getFileContentBinary(file);
        String contentType = getFileContentType(file);

        String responseText = """
        HTTP/1.1 200 OK
        Content-Type: %s
        Content-Length: %d
        
        """.formatted(contentType, fileContent.length);

        byte[] responseTextBytes = responseText.getBytes(StandardCharsets.UTF_8);
        byte[] response = new byte[responseTextBytes.length + fileContent.length];

        System.arraycopy(responseTextBytes, 0, response, 0, responseTextBytes.length);
        System.arraycopy(fileContent, 0, response, responseTextBytes.length, fileContent.length);

        return response;
    }
}
