package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Utility class for performing common Input/Output operations.
 * This class provides methods to handle {@link InputStream} reading and conversion.
 */
@UtilityClass
public class IOUtils {
    /**
     * Converts the content of the provided {@link InputStream} into a string
     * using the specified {@link Charset}.
     *
     * @param stream   the input stream from which data will be read. Must not be null.
     * @param charsets the character set used to decode the input stream. Must not be null.
     * @return a string created from the content of the input stream using the specified charset.
     * @throws IOException if an I/O error occurs during reading from the input stream.
     */
    public static String toString(final InputStream stream, final Charset charsets) throws IOException {
        return new String(readAllBytes(stream), charsets);
    }

    /**
     * Reads all bytes from the given InputStream and returns them as a byte array.
     * This method blocks until all bytes are read or an end-of-stream is reached.
     *
     * @param is the InputStream to read bytes from
     * @return a byte array containing all the read bytes from the InputStream
     * @throws IOException if an I/O error occurs during reading
     */
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
