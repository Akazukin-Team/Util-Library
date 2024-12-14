package org.akazukin.util.utils;

import java.util.Base64;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EncodeUtils {
    private static final Pattern base64pattern = Pattern.compile("^[a-z0-9A-Z/+]+={1,2}$");
    private static final byte[] DECODE_TABLE = {
            //   0   1   2   3   4   5   6   7   8   9   A   B   C   D   E   F
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 00-0f
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, // 10-1f
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, // 20-2f + - /
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, // 30-3f 0-9
            -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, // 40-4f A-O
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, // 50-5f P-Z _
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, // 60-6f a-o
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51                      // 70-7a p-z
    };

    /**
     * Is str Base64 format
     *
     * @param str array of Base64
     * @return true if str is Base64
     */
    public static boolean isBase64(final String str) {
        if (str == null) return false;
        return EncodeUtils.isBase64(str.getBytes()) && EncodeUtils.base64pattern.matcher(str).matches();
    }

    /**
     * Is array Base64 format
     *
     * @param array Base64 byte[]
     * @return true if array is Base64
     */
    public static boolean isBase64(final byte[] array) {
        if (array == null) return false;
        if (array.length < 1) return false;
        for (final byte b : array) {
            if (!(b == '=' || (b >= 0 && b < EncodeUtils.DECODE_TABLE.length && EncodeUtils.DECODE_TABLE[b] != -1)) &&
                    !(b == ' ' || b == '\n' || b == '\r' || b == '\t')) {
                return false;
            }
        }
        return true;
    }

    public static byte[] decodeBase64(final String str) {
        return Base64.getDecoder().decode(str);
    }

    public static byte[] decodeBase64(final byte[] str) {
        return Base64.getDecoder().decode(str);
    }

    public static String decodeBase64ToString(final byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] encodeBase64(final byte[] bytes) {
        return Base64.getEncoder().encode(bytes);
    }

    public static String encodeBase64ToString(final byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
