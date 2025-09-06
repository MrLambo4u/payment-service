package dispatcher;

import java.io.File;
import java.io.IOException;
import java.nio.channels.UnsupportedAddressTypeException;

import static util.FileUtils.*;

public class TextResponser implements Responser<String> {
    private static final String ERROR_RESPONSE_404 = "HTTP1.1/ 404 Not found";

    public String getResponse(File file) {
        try {
            return file.exists()
                    ? getSuccessResponse(file)
                    : ERROR_RESPONSE_404;
        } catch (UnsupportedAddressTypeException | IOException e) {
            return ERROR_RESPONSE_404;
        }
    }

    private String getSuccessResponse(File file) throws IOException {
        String contentType = getFileContentType(file);
        return """
                HTTP1.1/ 200 OK
                Content-type: %s; charset=UTF-8
                Content-length: %d
                
                %s
                """.formatted(contentType, file.length(), getFileContentText(file));
    }

    public String getErrorResponse404() {
        return ERROR_RESPONSE_404;
    }
}
