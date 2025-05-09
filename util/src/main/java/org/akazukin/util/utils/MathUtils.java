package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;

/**
 * Utility class providing mathematical operations and calculations.
 * This class includes methods for range validation, clamping values,
 * calculating maximum and minimum for variable-length arguments, and
 * specialized number manipulations such as rounding and formatting.
 * Each method is designed to handle various numerical types such as
 * integers, floats, doubles, and longs.
 */
@UtilityClass
public class MathUtils {
    /**
     * Determines if a given value lies within the range specified by two boundaries.
     * The range is inclusive and automatically determines the minimum and maximum boundaries
     * regardless of the order they are provided.
     *
     * @param value  the value to be checked
     * @param range  the first boundary of the range
     * @param range2 the second boundary of the range
     * @return true if the value lies within the specified range (inclusive), false otherwise
     */
    public static boolean contains(final int value, final int range, final int range2) {
        final int min = Math.min(range, range2);
        final int max = Math.max(range, range2);
        return min <= value && value <= max;
    }

    /**
     * Determines if a given value lies within the range specified by two boundaries.
     * The range is inclusive and automatically determines the minimum and maximum boundaries
     * regardless of the order they are provided.
     *
     * @param value  the value to be checked
     * @param range  the first boundary of the range
     * @param range2 the second boundary of the range
     * @return true if the value lies within the specified range (inclusive), false otherwise
     */
    public static boolean contains(final float value, final float range, final float range2) {
        final float min = Math.min(range, range2);
        final float max = Math.max(range, range2);
        return min <= value && value <= max;
    }

    /**
     * Determines if a given value lies within the range specified by two boundaries.
     * The range is inclusive and automatically determines the minimum and maximum boundaries
     * regardless of the order they are provided.
     *
     * @param value  the value to be checked
     * @param range  the first boundary of the range
     * @param range2 the second boundary of the range
     * @return true if the value lies within the specified range (inclusive), false otherwise
     */
    public static boolean contains(final double value, final double range, final double range2) {
        final double min = Math.min(range, range2);
        final double max = Math.max(range, range2);
        return min <= value && value <= max;
    }

    /**
     * Clamps a given integer value within the range specified by two boundary values.
     * The range is inclusive and automatically determines the minimum and maximum boundary
     * regardless of order.
     *
     * @param amount the value to be clamped
     * @param range  the first boundary of the clamping range
     * @param range2 the second boundary of the clamping range
     * @return the clamped value that lies within the specified range
     */
    public static int clamp(final int amount, final int range, final int range2) {
        final int min = Math.min(range, range2);
        final int max = Math.max(range, range2);
        return Math.min(Math.max(amount, min), max);
    }

    /**
     * Clamps a given integer value within the range specified by two boundary values.
     * The range is inclusive and automatically determines the minimum and maximum boundary
     * regardless of order.
     *
     * @param amount the value to be clamped
     * @param range  the first boundary of the clamping range
     * @param range2 the second boundary of the clamping range
     * @return the clamped value that lies within the specified range
     */
    public static double clamp(final double amount, final double range, final double range2) {
        final double min = Math.min(range, range2);
        final double max = Math.max(range, range2);
        return Math.min(Math.max(amount, min), max);
    }

    /**
     * Clamps a given integer value within the range specified by two boundary values.
     * The range is inclusive and automatically determines the minimum and maximum boundary
     * regardless of order.
     *
     * @param amount the value to be clamped
     * @param range  the first boundary of the clamping range
     * @param range2 the second boundary of the clamping range
     * @return the clamped value that lies within the specified range
     */
    public static float clamp(final float amount, final float range, final float range2) {
        final float min = Math.min(range, range2);
        final float max = Math.max(range, range2);
        return Math.min(Math.max(amount, min), max);
    }

    /**
     * Bring value closer to base by percent
     *
     * @param value   The number of doubles
     * @param base    The number of doubles
     * @param percent The number of doubles is that max is 1 and min is 0
     * @return The value that brings value closer to base by percent
     */
    public static float closer(final float value, final float base, final float percent) {
        return base - ((base - value) * percent);
    }

    /**
     * Bring value closer to base by percent
     *
     * @param value   The number of doubles
     * @param base    The number of doubles
     * @param percent The number of doubles is that max is 1 and min is 0
     * @return The value that brings value closer to base by percent
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
     * @param n The number of ints
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
     * @param n The number of ints
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
     * @param n The numbers of doubles
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
     * @param n The numbers of doubles
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
     * @param n The numbers of floats
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
     * @param n The numbers of floats
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
     * @param n The numbers of longs
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
     * @param n The numbers of longs
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

    /**
     * Formats a given floating-point value to a specified number of decimal places.
     *
     * @param value        the floating-point number to format
     * @param decimalPoint the number of decimal places to round the value to
     * @return the formatted floating-point value with the specified number of decimal places
     */
    public static double formatDecimal(final double value, final int decimalPoint) {
        final int dec = (int) Math.pow(10, decimalPoint);
        return (double) Math.round(value * dec) / dec;
    }

    /**
     * Formats a given floating-point value to a specified number of decimal places.
     *
     * @param value        the floating-point number to format
     * @param decimalPoint the number of decimal places to round the value to
     * @return the formatted floating-point value with the specified number of decimal places
     */
    public static float formatDecimal(final float value, final int decimalPoint) {
        final int dec = (int) Math.pow(10, decimalPoint);
        return (float) Math.round(value * dec) / dec;
    }

    /**
     * Performs division of two numbers and adjusts the result to the next integer if there is a remainder.
     *
     * @param dividend the number to be divided
     * @param divisor  the number by which to divide the dividend
     * @return the quotient of the division, rounded up to the next integer if there is a remainder
     */
    public int divisionWithCarry(final double dividend, final double divisor) {
        final int quotientInt = (int) (dividend / divisor);
        if (dividend == quotientInt * divisor) {
            return quotientInt;
        } else {
            return quotientInt + 1;
        }
    }

    /**
     * Performs division of two numbers and adjusts the result to the next integer if there is a remainder.
     *
     * @param dividend the number to be divided
     * @param divisor  the number by which to divide the dividend
     * @return the quotient of the division, rounded up to the next integer if there is a remainder
     */
    public int divisionWithCarry(final float dividend, final float divisor) {
        final int quotientInt = (int) (dividend / divisor);
        if (dividend == quotientInt * divisor) {
            return quotientInt;
        } else {
            return quotientInt + 1;
        }
    }

    /**
     * Performs division of two numbers and adjusts the result to the next integer if there is a remainder.
     *
     * @param dividend the number to be divided
     * @param divisor  the number by which to divide the dividend
     * @return the quotient of the division, rounded up to the next integer if there is a remainder
     */
    public int divisionWithCarry(final int dividend, final int divisor) {
        final int quotientInt = dividend / divisor;
        if (dividend == quotientInt * divisor) {
            return quotientInt;
        } else {
            return quotientInt + 1;
        }
    }

    /**
     * Performs division of two numbers and adjusts the result to the next integer if there is a remainder.
     *
     * @param dividend the number to be divided
     * @param divisor  the number by which to divide the dividend
     * @return the quotient of the division, rounded up to the next integer if there is a remainder
     */
    public long divisionWithCarry(final long dividend, final long divisor) {
        final long quotientInt = dividend / divisor;
        if (dividend == quotientInt * divisor) {
            return quotientInt;
        } else {
            return quotientInt + 1L;
        }
    }
}
