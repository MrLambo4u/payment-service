package dispatcher;

import java.io.File;
import java.io.IOException;
import java.nio.channels.UnsupportedAddressTypeException;

import static util.Constant.HTTP_404_ERROR_RESPONSE;
import static util.FileUtils.getFileContentText;
import static util.FileUtils.getFileContentType;

public class TextResponser implements Responser<String> {

    public String getResponse(File file) {
        try {
            return file.exists()
                    ? getSuccessResponse(file)
                    : HTTP_404_ERROR_RESPONSE;
        } catch (UnsupportedAddressTypeException | IOException e) {
            return HTTP_404_ERROR_RESPONSE;
        }
    }

    private String getSuccessResponse(File file) throws IOException {
        String contentType = getFileContentType(file);
        return """
                HTTP/1.1 200 OK
                Content-type: %s; charset=UTF-8
                Content-length: %d
                
                %s
                """.formatted(contentType, file.length(), getFileContentText(file));
    }

    public String getErrorResponse404() {
        return HTTP_404_ERROR_RESPONSE;
    }
}
