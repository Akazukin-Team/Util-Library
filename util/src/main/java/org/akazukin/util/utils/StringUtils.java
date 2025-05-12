package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class StringUtils {
    public static final String[] EMPTY_ARRAY = {};
    private static final MessageDigest sha3_512;

    static {
        try {
            sha3_512 = MessageDigest.getInstance("SHA3-512");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static int getLimitedLevenshteinDistance(CharSequence left, CharSequence right, final int threshold) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
        }

        int n = left.length();
        int m = right.length();

        if (n == 0) {
            return m <= threshold ? m : -1;
        }
        if (m == 0) {
            return n <= threshold ? n : -1;
        }

        if (n > m) {
            final CharSequence tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
        }

        if (m - n > threshold) {
            return -1;
        }

        int[] p = new int[n + 1];
        int[] d = new int[n + 1];
        int[] tempD;

        final int boundary = Math.min(n, threshold) + 1;
        for (int i = 0; i < boundary; i++) {
            p[i] = i;
        }

        Arrays.fill(p, boundary, p.length, Integer.MAX_VALUE);
        Arrays.fill(d, Integer.MAX_VALUE);

        for (int j = 1; j <= m; j++) {
            final char rightJ = right.charAt(j - 1);
            d[0] = j;

            final int min = Math.max(1, j - threshold);
            final int max = j > Integer.MAX_VALUE - threshold ? n : Math.min(
                    n, j + threshold);

            if (min > 1) {
                d[min - 1] = Integer.MAX_VALUE;
            }

            int lowerBound = Integer.MAX_VALUE;
            for (int i = min; i <= max; i++) {
                if (left.charAt(i - 1) == rightJ) {
                    d[i] = p[i - 1];
                } else {
                    d[i] = 1 + Math.min(Math.min(d[i - 1], p[i]), p[i - 1]);
                }
                lowerBound = Math.min(lowerBound, d[i]);
            }
            if (lowerBound > threshold) {
                return -1;
            }

            tempD = p;
            p = d;
            d = tempD;
        }

        if (p[n] <= threshold) {
            return p[n];
        }
        return -1;
    }

    public static int getLevenshteinDistance(CharSequence left, CharSequence right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("CharSequences must not be null");
        }

        int n = left.length();
        int m = right.length();

        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }

        if (n > m) {
            final CharSequence tmp = left;
            left = right;
            right = tmp;
            n = m;
            m = right.length();
        }

        final int[] p = new int[n + 1];

        int i;
        int j;
        int upperLeft;
        int upper;

        char rightJ;
        int cost;

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            upperLeft = p[0];
            rightJ = right.charAt(j - 1);
            p[0] = j;

            for (i = 1; i <= n; i++) {
                upper = p[i];
                cost = left.charAt(i - 1) == rightJ ? 0 : 1;
                p[i] = Math.min(Math.min(p[i - 1] + 1, p[i] + 1), upperLeft + cost);
                upperLeft = upper;
            }
        }

        return p[n];
    }

    @NotNull
    public static String toSHA(@NotNull final CharSequence s) {
        final byte[] bytes = s.toString().getBytes(StandardCharsets.UTF_8);
        final byte[] result = StringUtils.sha3_512.digest(bytes);

        return String.format("%040x", new BigInteger(1, result));
    }

    @NotNull
    public static String toStringOrEmpty(final CharSequence s) {
        return s != null ? s.toString() : "";
    }

    @NotNull
    public static String randomString(final int length) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int id = (int) (Math.random() * 62) + 48;
            if (id >= 58) {
                id += 7;
            }
            if (id >= 91) {
                id += 6;
            }
            sb.append((char) id);
        }
        return sb.toString();
    }

    @NotNull
    public static String toString(final Throwable t) {
        try (final StringWriter sw = new StringWriter()) {
            try (final PrintWriter pw = new PrintWriter(sw)) {
                t.printStackTrace(pw);
                return sw.toString();
            }
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Nullable
    public static String substring(@Nullable final CharSequence s, final int start, final int end) {
        if (s == null) {
            return null;
        }
        final String s2 = s.toString();
        if (isEmpty(s2) || (start == 0 && s.length() <= end)) {
            return s2;
        } else {
            return s2.substring(start, Math.min(end, s.length()));
        }
    }

    public static boolean isEmpty(@Nullable final CharSequence s) {
        return getLength(s) <= 0;
    }

    public static int getLength(@Nullable final CharSequence s) {
        return s == null ? -1 : s.length();
    }

    @Nullable
    public static String[] split(@Nullable final CharSequence s, final char separatorChar) {
        return split(s, separatorChar, false);
    }

    /**
     * Performs the logic for the {@code split} and
     * {@code splitPreserveAllTokens} methods that do not return a
     * maximum array length.
     *
     * @param str               the String to parse, may be {@code null}
     * @param separatorChar     the separate character
     * @param preserveAllTokens if {@code true}, adjacent separators are
     *                          treated as empty token separators; if {@code false}, adjacent
     *                          separators are treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    @Nullable
    public static String[] split(@Nullable final CharSequence str, final char separatorChar, final boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return EMPTY_ARRAY;
        }

        final String str2 = str.toString();
        final List<String> list = new ArrayList<>();
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        int i;
        for (i = 0; i < len; ) {
            if (str2.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str2.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str2.substring(start, i));
        }
        return list.toArray(EMPTY_ARRAY);
    }
}
