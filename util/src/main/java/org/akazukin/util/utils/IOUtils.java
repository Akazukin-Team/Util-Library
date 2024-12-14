package org.akazukin.util.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import lombok.experimental.UtilityClass;

@UtilityClass
public class IOUtils {
    public static String toString(final InputStream stream, final Charset charsets) throws IOException {
        return new String(IOUtils.readAllBytes(stream), charsets);
    }

    public static byte[] readAllBytes(final InputStream is) throws IOException {
        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
        final byte[] data = new byte[1024 * 8]; //8KB
        int read;
        while ((read = is.read(data)) != -1) {
            buf.write(data, 0, read);
        }
        return buf.toByteArray();
    }
}
