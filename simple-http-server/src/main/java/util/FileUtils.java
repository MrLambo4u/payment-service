package util;

import java.io.File;
import java.io.IOException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static util.Constant.ContentType.*;
import static util.Constant.Symbols.DOT;

public class FileUtils {
    public static String getFileContentType(File file) {
        String contentType = FILE_TYPE_TO_CONTENT_TYPE_MAP.get(file.getName().split(DOT)[1]);
        if (contentType == null) {
            throw new UnsupportedAddressTypeException();
        }
        return contentType;
    }

    public static String getFileContentText(File file) throws IOException {
        if (!file.exists()) {
            throw new NoSuchFileException(file.getName());
        }
        byte[] bytes = readAllBytes(Path.of(file.getPath()));
        return new String(bytes, UTF_8);
    }

    public static byte[] getFileContentBinary(File file) throws IOException {
        if (!file.exists()) {
            throw new NoSuchFileException(file.getName());
        }
        return readAllBytes(Path.of(file.getPath()));
    }

    public static boolean isBinaryFileByContentType(String type) {
        return BINARY_CONTENT_TYPE_SUPPORTED_SET.contains(type);
    }

    public static boolean isTextFileByContentType(String type) {
        return NON_BINARY_CONTENT_TYPE_SUPPORTED_SET.contains(type);
    }
}
