package dispatcher;

import java.nio.charset.StandardCharsets;

public class BinaryResponser implements Responser<byte[]> {
    public byte[] getResponse(byte[] content, String contentType) {
        String responseText = """
                HTTP/1.1 200 OK
                Content-Type: %s
                Content-Length: %d
                
                """.formatted(contentType, content.length);

        byte[] responseTextBytes = responseText.getBytes(StandardCharsets.UTF_8);
        byte[] response = new byte[responseTextBytes.length + content.length];

        System.arraycopy(responseTextBytes, 0, response, 0, responseTextBytes.length);
        System.arraycopy(content, 0, response, responseTextBytes.length, content.length);

        return response;
    }
}
