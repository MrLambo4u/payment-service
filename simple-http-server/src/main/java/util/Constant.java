package util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface Constant {
    int DEFAULT_PORT = 8080;
    String PATH_STATIC_FOLDER = new File("").getAbsolutePath().concat("/simple-http-server/src/main/resources/static");
    String HTTP_404_ERROR_RESPONSE = "HTTP/1.1 404 Not found";

    interface Symbols {
        String SPACE = " ";
        String DOT = "\\.";
    }

    interface ContentType {
        Map<String, String> FILE_TYPE_TO_CONTENT_TYPE_MAP = new HashMap<>() {{
            put("html", "text/html");
            put("css", "text/css");
            put("js", "application/javascript");
            put("png", "image/png");
        }};

        Set<String> BINARY_CONTENT_TYPE_SUPPORTED_SET = Set.of("image/png");
        Set<String> NON_BINARY_CONTENT_TYPE_SUPPORTED_SET = Set.of("text/html", "text/css", "application/javascript");
    }
}
