package org.akazukin.util.utils;

import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RandomUtils {
    public static final Random RANDOM = new Random();
    private static final String BadBound = "bound must be positive";

    public static boolean nextBoolean() {
        return RandomUtils.RANDOM.nextBoolean();
    }

    public static double nextDouble(final double d1, final double d2) {
        return (RandomUtils.RANDOM.nextDouble() * (Math.max(d1, d2) - Math.min(d1, d2))) + Math.min(d1, d2);
    }

    public static long nextLong(final long l1, final long l2) {
        return nextLong(Math.max(l1, l2) - Math.min(l1, l2)) + Math.min(l1, l2);
    }

    public static long nextLong(final long bound) {
        if (bound <= 0L)
            throw new IllegalArgumentException(BadBound);

        long r = RandomUtils.RANDOM.nextLong();
        final long m = bound - 1L;

        if ((bound & m) == 0L)
            r = r >>> Long.numberOfLeadingZeros(m) & m;
        else {
            for (long u = r >>> 1;
                 u + m - (r = u % bound) < 0;
                 u = RandomUtils.RANDOM.nextLong() >>> 1)
                ;
        }
        return r;
    }

    public static float nextFloat(final float f1, final float f2) {
        return (RandomUtils.RANDOM.nextFloat() * (Math.max(f1, f2) - Math.min(f1, f2))) + Math.min(f1, f2);
    }

    public static String randomString(final String chars, final int length) {
        final StringBuilder str = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            final int i2 = RandomUtils.nextInt(0, chars.length() - 1);
            str.append(chars.charAt(i2));
        }
        return str.toString();
    }

    public static int nextInt(final int i1, final int i2) {
        return RandomUtils.RANDOM.nextInt(Math.max(i1, i2) - Math.min(i1, i2)) + Math.min(i1, i2);
    }
}
