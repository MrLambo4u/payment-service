package dispatcher;

public class TextResponser implements Responser<String> {

    public String getResponse(String content, String contentType) {
        return """
                HTTP/1.1 200 OK
                Content-type: %s; charset=UTF-8
                Content-length: %d
                
                %s
                """.formatted(contentType, content.length(), content);
    }
}
