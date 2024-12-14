package org.akazukin.util.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class ObjectUtils {
    @Nullable
    public static Boolean getBoolean(final byte bool) {
        if (bool == 0) return false;
        else if (bool == 1) return true;
        return null;
    }

    @Nullable
    public static Boolean getBoolean(final String bool) {
        if ((bool).equalsIgnoreCase("true")) return true;
        else if ((bool).equalsIgnoreCase("false")) return false;
        return null;
    }

    @Nullable
    public static Byte toByte(final Boolean bool) {
        return bool != null ? (byte) (bool ? 1 : 0) : null;
    }

    public static String toString(final InputStream stream, final Charset charsets) throws IOException {
        return ObjectUtils.toString(IOUtils.readAllBytes(stream), charsets);
    }

    public static String toString(final byte[] bytes, final Charset charsets) {
        return new String(bytes, charsets);
    }
}
