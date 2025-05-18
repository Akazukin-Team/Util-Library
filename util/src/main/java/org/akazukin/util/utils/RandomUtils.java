package org.akazukin.util.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RandomUtils {
    private static final String BadBound = "bound must be positive";
    private static final long serialVersionUID = -2560973338619712162L;

    Random random;

    /**
     * Constructs an instance of {@link RandomUtils} using the specified {@link Random} instance.
     * This constructor allows the randomness source for the utility methods to be customized.
     *
     * @param random the {@link Random} instance used as the source of randomness.
     *               Must not be {@code null}.
     */
    public RandomUtils(@NotNull final Random random) {
        this.random = random;
    }

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

        final float rdm = this.random.nextFloat();

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

        return min + this.random.nextInt(leftDiff) + this.random.nextInt(rightDiff);
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

        final double rdm = this.random.nextDouble();

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
     * If {@code random} is an instance of {@link ThreadLocalRandom}, it uses the {@link ThreadLocalRandom#nextLong(long)} method.
     *
     * @param bound the upper bound (exclusive) for the generated random value. Must be positive.
     * @return a random long value between 0 (inclusive) and {@code bound} (exclusive).
     * @throws IllegalArgumentException if {@code bound} is less than or equal to 0.
     */
    public long nextLong(final long bound) {
        if (this.random instanceof ThreadLocalRandom) {
            ((ThreadLocalRandom) this.random).nextLong(bound);
        }

        if (bound <= 0L) {
            throw new IllegalArgumentException(BadBound);
        }

        long r = this.random.nextLong();
        final long m = bound - 1L;

        if ((bound & m) == 0L) {
            r = r >>> Long.numberOfLeadingZeros(m) & m;
        } else {
            for (long u = r >>> 1;
                 u + m - (r = u % bound) < 0;
                 u = this.random.nextLong() >>> 1) {
            }
        }
        return r;
    }

    /**
     * Selects a random character from the provided string.
     *
     * @param str the string containing characters to select from.
     *            Must not be {@code null} or empty.
     * @return a randomly selected character from the input string.
     * The method may throw an exception if the input string is empty.
     */
    public char nextChar(@NotNull final String str) {
        return this.nextChar(str.toCharArray());
    }

    /**
     * Selects a random character from the provided array of characters.
     *
     * @param chars the array of characters to select from.
     *              This must not be {@code null} or empty.
     * @return a randomly selected character from the input array.
     * If the array is empty, the method may throw an exception, subject to implementation details.
     */
    public char nextChar(final char[] chars) {
        return chars[this.nextInt(0, chars.length - 1)];
    }

    /**
     * Generates a random string of the specified length using characters from the input string.
     * Each character in the resulting string is randomly selected from the input string.
     *
     * @param str    the input string containing characters to be used for generating the random string.
     *               Must not be {@code null}.
     * @param length the desired length of the random string to be generated.
     *               Must be a non-negative value.
     * @return a randomly generated string of the specified length, composed of characters from the input string.
     * If the length is zero, an empty string is returned.
     * This method internally delegates to {@link #nextChar(char[], int)} by converting the input string to a character array.
     */
    public String nextChar(@NotNull final String str, @Range(from = 0, to = Integer.MAX_VALUE) final int length) {
        return this.nextChar(str.toCharArray(), length);
    }

    /**
     * Generates a random string of the specified length using the provided array of characters.
     * Each character in the resulting string is selected randomly from the input array.
     *
     * @param chars  the array of characters to be used for generating the random string.
     *               This must not be {@code null}.
     * @param length the desired length of the random string to be generated.
     *               Must be a non-negative value.
     * @return a randomly generated string of the specified length, composed of characters from the input array.
     * If the input array is empty or the length is zero, an empty string is returned.
     */
    public String nextChar(final char[] chars, @Range(from = 0, to = Integer.MAX_VALUE) final int length) {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(chars[this.nextInt(0, chars.length - 1)]);
        }
        return str.toString();
    }

    /**
     * Selects a random element from the provided array of {@link CharSequence} objects.
     *
     * @param chars the array of {@link CharSequence} elements to select from.
     *              This must not be {@code null} or empty.
     * @return a randomly selected element from the input array.
     * The returned element is of type {@link T}.
     * @throws IllegalArgumentException if {@code chars} is empty.
     */
    public <T extends CharSequence> T nextChar(@NotNull final T[] chars) {
        return chars[this.nextInt(0, chars.length - 1)];
    }

    /**
     * Generates a random string of the specified length using the provided array of characters.
     * Each character in the resulting string is selected randomly from the input array.
     *
     * @param chars  the array of {@link T} elements from which characters will be randomly selected.
     *               This must not be {@code null}.
     * @param length the desired length of the generated string.
     *               Must be non-negative.
     * @return a randomly generated string of the specified length, composed of characters from the input array.
     * If the length is zero, an empty string is returned.
     */
    public <T extends CharSequence> String nextChar(@NotNull final T[] chars, @Range(from = 0, to = Integer.MAX_VALUE) final int length) {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(chars[this.nextInt(0, chars.length - 1)]);
        }
        return str.toString();
    }
}
