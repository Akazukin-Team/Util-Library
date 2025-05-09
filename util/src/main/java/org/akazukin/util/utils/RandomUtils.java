package org.akazukin.util.utils;

import java.util.Random;

public class RandomUtils extends Random {
    private static final String BadBound = "bound must be positive";
    private static final long serialVersionUID = -2560973338619712162L;

    /**
     * Generates a random float value constrained between two provided float values.
     * The method ensures the generated value lies within the range [min, max], where min and max
     * are the smaller and larger of the two input parameters respectively.
     * If the input parameters are equal, the method returns 0.
     *
     * @param f1 the first boundary float value, which determines one edge of the range.
     * @param f2 the second boundary float value, which determines the other edge of the range.
     * @return a random float value between the range defined by the inputs, with the lower bound
     * inclusive and the upper bound exclusive. Returns 0 if the inputs are the same.
     */
    public float nextFloat(final float f1, final float f2) {
        if (f1 == f2) {
            return 0;
        }
        final float max = Math.max(f1, f2);
        final float min = Math.min(f1, f2);

        final float rdm = this.nextFloat();

        // 大体半分の値からランダムの値を作る(オーバーフロー対策)
        final float leftDiff = (max / 2) - (min / 2);
        final float rightDiff = max - (min + leftDiff);

        return min + (leftDiff * rdm) + (rightDiff * rdm);
    }

    /**
     * Generates a random string of a specified length using the provided set of characters.
     * Each character in the resulting string is randomly selected from the input string of characters.
     *
     * @param chars  the string containing characters to be used for generating the random string.
     *               Must not be null or empty.
     * @param length the desired length of the random string to be generated. Must be non-negative.
     * @return a randomly generated string of the specified length, composed of characters from the input string.
     * If the input character set is empty or the length is zero, an empty string is returned.
     */
    public String randomString(final String chars, final int length) {
        final StringBuilder str = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            final int i2 = this.nextInt(0, chars.length() - 1);
            str.append(chars.charAt(i2));
        }
        return str.toString();
    }

    /**
     * Generates a random integer value constrained between two provided integer values.
     * The method ensures the generated value lies within the range [min, max], where
     * min and max are the smaller and larger of the two input parameters respectively.
     * If the input parameters are equal, the method returns 0.
     *
     * @param i1 the first boundary integer value, which determines one edge of the range.
     * @param i2 the second boundary integer value, which determines the other edge of the range.
     * @return a random integer value within the range defined by the inputs, with the lower bound
     * inclusive and the upper bound exclusive. Returns 0 if the inputs are the same.
     */
    public int nextInt(final int i1, final int i2) {
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

        return min + this.nextInt(leftDiff) + this.nextInt(rightDiff);
    }

    /**
     * Generates a random double value constrained between two provided double values.
     * The method ensures the generated value lies within the range [min, max], where
     * min and max are the smaller and larger of the two input parameters respectively.
     * If the input parameters are equal, the method returns 0.
     *
     * @param d1 the first boundary double value, which determines one edge of the range
     * @param d2 the second boundary double value, which determines the other edge of the range
     * @return a random double value within the range defined by the inputs, with the lower bound
     * inclusive and the upper bound exclusive. Returns 0 if the inputs are the same.
     */
    public double nextDouble(final double d1, final double d2) {
        if (d1 == d2) {
            return 0;
        }
        final double max = Math.max(d1, d2);
        final double min = Math.min(d1, d2);

        final double rdm = this.nextDouble();

        // 大体半分の値からランダムの値を作る(オーバーフロー対策)
        final double leftDiff = (max / 2) - (min / 2);
        final double rightDiff = max - (min + leftDiff);

        return min + (leftDiff * rdm) + (rightDiff * rdm);
    }

    /**
     * Generates a random long value constrained between two provided long values.
     * The method ensures the generated value lies within the range [min, max],
     * where min and max are the smaller and larger of the two input parameters respectively.
     * If the input parameters are equal, the method returns 0.
     *
     * @param l1 the first boundary-long value, which determines one edge of the range
     * @param l2 the second boundary-long value, which determines the other edge of the range
     * @return a random long value within the range defined by the inputs,
     * with the lower bound inclusive and the upper bound exclusive.
     * Returns 0 if the inputs are the same.
     */
    public long nextLong(final long l1, final long l2) {
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

        return min + this.nextLong(leftDiff) + this.nextLong(rightDiff);
    }

    /**
     * Generates a random long value constrained between 0 (inclusive) and the specified {@code bound} (exclusive).
     * The method ensures the generated value is within the range [0, bound].
     *
     * @param bound the upper bound (exclusive) for the generated random value. Must be positive.
     * @return a random long value between 0 (inclusive) and {@code bound} (exclusive).
     * @throws IllegalArgumentException if {@code bound} is less than or equal to 0.
     */
    public long nextLong(final long bound) {
        if (bound <= 0L) {
            throw new IllegalArgumentException(BadBound);
        }

        long r = this.nextLong();
        final long m = bound - 1L;

        if ((bound & m) == 0L) {
            r = r >>> Long.numberOfLeadingZeros(m) & m;
        } else {
            for (long u = r >>> 1;
                 u + m - (r = u % bound) < 0;
                 u = this.nextLong() >>> 1) {
            }
        }
        return r;
    }
}
