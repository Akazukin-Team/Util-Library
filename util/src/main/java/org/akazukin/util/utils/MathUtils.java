package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathUtils {
    public static boolean contains(final int value, final int range, final int range2) {
        return Math.min(range, range2) <= value && value <= Math.max(range, range2);
    }

    public static int clamp(final int amount, final int range, final int range2) {
        final int min = Math.min(range, range2);
        final int max = Math.max(range, range2);
        return Math.min(Math.max(amount, min), max);
    }

    public static double clamp(final double amount, final double range, final double range2) {
        final double min = Math.min(range, range2);
        final double max = Math.max(range, range2);
        return Math.min(Math.max(amount, min), max);
    }

    public static float clamp(final float amount, final float range, final float range2) {
        final float min = Math.min(range, range2);
        final float max = Math.max(range, range2);
        return Math.min(Math.max(amount, min), max);
    }

    public static float closer(final float value, final float base, final float percent) {
        return base - ((base - value) * percent);
    }

    /**
     * Bring value closer to base by percent
     *
     * @param value   The number of double
     * @param base    The number of double
     * @param percent The number of double is that max is 1 and min is 0
     * @return The value that bring value closer to base by percent
     */
    public static double closer(final double value, final double base, final double percent) {
        if (value == base) {
            return value;
        }
        return base - ((base - value) * percent);
    }

    /**
     * Method like {@link java.lang.Math#max(int, int) max} for Variable-Length Arguments
     *
     * @param n The number of int
     * @return The maximum of n
     * @throws java.lang.IllegalArgumentException The length of n is 0
     */
    public static int max(final int... n) {
        if (n.length == 0) {
            throw new IllegalArgumentException("The specified number length is 0");
        }
        int max = n[0];
        for (final int v : n) {
            max = Math.max(max, v);
        }
        return max;
    }

    /**
     * Method like {@link java.lang.Math#min(int, int) min} for Variable-Length Arguments
     *
     * @param n The number of int
     * @return The minimum of n
     * @throws java.lang.IllegalArgumentException The length of n is 0
     */
    public static int min(final int... n) {
        if (n.length == 0) {
            throw new IllegalArgumentException("The specified number length is 0");
        }
        int min = n[0];
        for (final int v : n) {
            min = Math.min(min, v);
        }
        return min;
    }

    /**
     * Method like {@link java.lang.Math#max(double, double) max} for Variable-Length Arguments
     *
     * @param n The numbers of double
     * @return The maximum of n
     * @throws java.lang.IllegalArgumentException The length of n is 0
     */
    public static double max(final double... n) {
        if (n.length == 0) {
            throw new IllegalArgumentException("The specified number length is 0");
        }
        double max = n[0];
        for (final double v : n) {
            max = Math.max(max, v);
        }
        return max;
    }

    /**
     * Method like {@link java.lang.Math#min(double, double) min} for Variable-Length Arguments
     *
     * @param n The numbers of double
     * @return The minimum of n
     * @throws java.lang.IllegalArgumentException The length of n is 0
     */
    public static double min(final double... n) {
        if (n.length == 0) {
            throw new IllegalArgumentException("The specified number length is 0");
        }
        double min = n[0];
        for (final double v : n) {
            min = Math.min(min, v);
        }
        return min;
    }

    /**
     * Method like {@link java.lang.Math#max(float, float) max} for Variable-Length Arguments
     *
     * @param n The numbers of float
     * @return The maximum of n
     * @throws java.lang.IllegalArgumentException The length of n is 0
     */
    public static float max(final float... n) {
        if (n.length == 0) {
            throw new IllegalArgumentException("The specified number length is 0");
        }
        float max = n[0];
        for (final float v : n) {
            max = Math.max(max, v);
        }
        return max;
    }

    /**
     * Method like {@link java.lang.Math#min(float, float) min} for Variable-Length Arguments
     *
     * @param n The numbers of float
     * @return The minimum of n
     * @throws java.lang.IllegalArgumentException The length of n is 0
     */
    public static float min(final float... n) {
        if (n.length == 0) {
            throw new IllegalArgumentException("The specified number length is 0");
        }
        float min = n[0];
        for (final float v : n) {
            min = Math.min(min, v);
        }
        return min;
    }

    /**
     * Method like {@link java.lang.Math#max(long, long) max} for Variable-Length Arguments
     *
     * @param n The numbers of long
     * @return The maximum of n
     * @throws java.lang.IllegalArgumentException The length of n is 0
     */
    public static long max(final long... n) {
        if (n.length == 0) {
            throw new IllegalArgumentException("The specified number length is 0");
        }
        long max = n[0];
        for (final long v : n) {
            max = Math.max(max, v);
        }
        return max;
    }

    /**
     * Method like {@link java.lang.Math#min(long, long) min} for Variable-Length Arguments
     *
     * @param n The numbers of long
     * @return The minimum of n
     * @throws java.lang.IllegalArgumentException The length of n is 0
     */
    public static long min(final long... n) {
        if (n.length == 0) {
            throw new IllegalArgumentException("The specified number length is 0");
        }
        long min = n[0];
        for (final long v : n) {
            min = Math.min(min, v);
        }
        return min;
    }

    public static double formatDecimal(final double value, final int decimalPoint) {
        final int dec = (int) Math.pow(10, decimalPoint);
        return (double) Math.round(value * dec) / dec;
    }

    public static float formatDecimal(final float value, final int decimalPoint) {
        final int dec = (int) Math.pow(10, decimalPoint);
        return (float) Math.round(value * dec) / dec;
    }
}
