package util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Constant {
    int DEFAULT_PORT = 8080;
    String PATH_STATIC_FOLDER = new File("").getAbsolutePath().concat("/simple-http-server/src/main/resources/static");

    interface Symbols {
        String SPACE = " ";
        String DOT = "\\.";
        String SHORT_YES = "Y";
        String SHORT_NO = "N";
    }

    interface ContentType {
        Map<String, String> FILE_TYPE_TO_CONTENT_TYPE_MAP = new HashMap<>() {{
            put("html", "text/html");
            put("css", "text/css");
            put("js", "application/javascript");
            put("png", "image/png");
        }};

        List<String> BINARY_CONTENT_TYPE_SUPPORTED_LIST = List.of("image/png");
        List<String> NON_BINARY_CONTENT_TYPE_SUPPORTED_LIST = List.of("text/html", "text/css", "application/javascript");
    }}
