package util;

import java.io.File;
import java.io.IOException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static java.util.Objects.isNull;
import static util.Constant.ContentType.FILE_TYPE_TO_CONTENT_TYPE_MAP;
import static util.Constant.Symbols.DOT;

public class FileUtils {
    public static String getFileContentType(File file) {
        String contentType = FILE_TYPE_TO_CONTENT_TYPE_MAP.get(file.getName().split(DOT)[1]);
        if (isNull(contentType)) {
            throw new UnsupportedAddressTypeException();
        }
        return contentType;
    }

    public static String getFileContentText(File file) throws IOException {
        byte[] bytes = readAllBytes(Path.of(file.getPath()));
        return new String(bytes, UTF_8);
    }

    public static byte[] getFileContentBinary(File file) throws IOException {
        return readAllBytes(Path.of(file.getPath()));
    }
}
