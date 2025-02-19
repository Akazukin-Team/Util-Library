package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomUtils {
    public static final Random RANDOM = new Random();
    private static final String BadBound = "bound must be positive";

    public static boolean nextBoolean() {
        return RandomUtils.RANDOM.nextBoolean();
    }

    public static double nextDouble(final double d1, final double d2) {
        final double min, max;
        if (d1 == d2) {
            return 0;
        } else if (d1 < d2) {
            min = d1;
            max = d2;
        } else {
            min = d2;
            max = d1;
        }
        final double rdm = RandomUtils.RANDOM.nextDouble();

        // 大体半分の値からランダムの値を作る(オーバーフロー対策)
        final double leftDiff = (max / 2) - (min / 2);
        final double rightDiff = max - (min + leftDiff);

        return min + (leftDiff * rdm) + (rightDiff * rdm);
    }

    public static long nextLong(final long l1, final long l2) {
        final long min, max;
        if (l1 == l2) {
            return 0;
        } else if (l1 < l2) {
            min = l1;
            max = l2;
        } else {
            min = l2;
            max = l1;
        }

        // 大体半分の値からランダムの値を作る(オーバーフロー対策)
        final long leftDiff = (max / 2) - (min / 2);
        final long rightDiff = max - (min + leftDiff);

        return min + nextLong(leftDiff) + nextLong(rightDiff);
    }

    public static long nextLong(final long bound) {
        if (bound <= 0L) {
            throw new IllegalArgumentException(BadBound);
        }

        long r = RandomUtils.RANDOM.nextLong();
        final long m = bound - 1L;

        if ((bound & m) == 0L) {
            r = r >>> Long.numberOfLeadingZeros(m) & m;
        } else {
            for (long u = r >>> 1;
                 u + m - (r = u % bound) < 0;
                 u = RandomUtils.RANDOM.nextLong() >>> 1) {
            }
        }
        return r;
    }

    public static float nextFloat(final float f1, final float f2) {
        final float min, max;
        if (f1 == f2) {
            return 0;
        } else if (f1 < f2) {
            min = f1;
            max = f2;
        } else {
            min = f2;
            max = f1;
        }
        final float rdm = RandomUtils.RANDOM.nextFloat();

        // 大体半分の値からランダムの値を作る(オーバーフロー対策)
        final float leftDiff = (max / 2) - (min / 2);
        final float rightDiff = max - (min + leftDiff);

        return min + (leftDiff * rdm) + (rightDiff * rdm);
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
        final int min, max;
        if (i1 == i2) {
            return 0;
        } else if (i1 < i2) {
            min = i1;
            max = i2;
        } else {
            min = i2;
            max = i1;
        }

        // 大体半分の値からランダムの値を作る(オーバーフロー対策)
        final int leftDiff = (max / 2) - (min / 2);
        final int rightDiff = max - (min + leftDiff);

        return min + nextInt(leftDiff) + nextInt(rightDiff);
    }

    public static int nextInt(final int bound) {
        return RandomUtils.RANDOM.nextInt(bound);
    }
}
