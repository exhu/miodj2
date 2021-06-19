package miod_parser;

import java.io.IOException;
import java.io.InputStream;

final class TestUtils {

    @Deprecated
    public static InputStream getStreamFromResource(String fileName) throws IOException {
        ClassLoader classLoader = TestUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        return inputStream;
    }
}