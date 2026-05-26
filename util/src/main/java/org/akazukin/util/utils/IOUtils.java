package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;

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
        return new String(stream.readAllBytes(), charsets);
    }
}
