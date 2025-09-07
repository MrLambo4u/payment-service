package dispatcher;

import java.util.HashMap;
import java.util.Map;

public interface Responser<T> {
    String HTTP_400_ERROR_RESPONSE = "HTTP/1.1 400 Bad Request";
    String HTTP_404_ERROR_RESPONSE = "HTTP/1.1 404 Not found";
    String HTTP_500_ERROR_RESPONSE = "HTTP/1.1 500 Internal Server Error";

    Integer BAD_REQUEST_STATUS = 400;
    Integer NOT_FOUND_STATUS = 404;
    Integer INTERNAL_SERVER_ERROR_STATUS = 500;

    Map<Integer, String> HTTP_STATUS_TO_ERROR_RESPONSE = new HashMap<>(){{
        put(BAD_REQUEST_STATUS, HTTP_400_ERROR_RESPONSE);
        put(NOT_FOUND_STATUS, HTTP_404_ERROR_RESPONSE);
        put(INTERNAL_SERVER_ERROR_STATUS, HTTP_500_ERROR_RESPONSE);
    }};

    T getResponse(T content, String contentType);

    static String getErrorResponseByHttpStatus(Integer code) {
        return HTTP_STATUS_TO_ERROR_RESPONSE.getOrDefault(code, HTTP_404_ERROR_RESPONSE);
    }
}
