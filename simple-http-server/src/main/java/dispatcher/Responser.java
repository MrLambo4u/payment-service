package dispatcher;

import java.io.File;

public interface Responser<T> {
    T getResponse(File file);
}
