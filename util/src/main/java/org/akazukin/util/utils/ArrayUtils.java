package org.akazukin.util.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * A utility class for performing various operations on arrays such as concatenation,
 * splitting, and type-specific manipulations.
 */
@UtilityClass
public class ArrayUtils {
    public static final String EX_ARRAY_NULL = "Array must not be null";
    public static final String EX_SIZE_NEGATIVE = "The size of array must be positive";

    /**
     * Concatenates multiple arrays of the same type into a single array.
     * The order of elements is preserved, with elements from earlier arrays
     * placed before those of later arrays.
     *
     * @param <T> the type of the array elements
     * @param arr a variable number of input arrays to concatenate.
     *            Each array must not be {@code null}.
     * @return a single array containing all elements from the provided arrays in order.
     * If no arrays are provided, an empty array will be returned.
     * Must not be {@code null}.
     * @throws NullPointerException if {@code arr} or any of the provided arrays is null.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public <T> T[] concat(@NonNull final T[]... arr) {
        int len = 0;
        for (final T[] subArr : arr) {
            if (subArr == null) {
                throw new NullPointerException(EX_ARRAY_NULL);
            }

            len += subArr.length;
        }

        final Class<T> type = (Class<T>) arr.getClass().getComponentType().getComponentType();
        final T[] res = (T[]) Array.newInstance(type, len);

        int pos = 0;
        for (final T[] subArr : arr) {
            System.arraycopy(subArr, 0, res, pos, subArr.length);
            pos += subArr.length;
        }

        return res;
    }

    /**
     * Concatenates multiple byte arrays into a single byte array.
     * The order of elements is preserved, with elements from earlier arrays
     * placed before those of later arrays.
     *
     * @param arr a variable number of input byte arrays to concatenate.
     *            Each array must not be {@code null}.
     * @return a single byte array containing all elements from the provided arrays in order.
     * If no arrays are provided, an empty byte array will be returned.
     * Must not be {@code null}.
     * @throws NullPointerException if {@code arr} or any of the provided arrays is null.
     */
    @NotNull
    public byte[] concat(@NonNull final byte[]... arr) {
        int len = 0;
        for (final byte[] subArr : arr) {
            if (subArr == null) {
                throw new NullPointerException(EX_ARRAY_NULL);
            }

            len += subArr.length;
        }

        final byte[] res = new byte[len];

        int pos = 0;
        for (final byte[] subArr : arr) {
            System.arraycopy(subArr, 0, res, pos, subArr.length);
            pos += subArr.length;
        }

        return res;
    }

    /**
     * Concatenates multiple char arrays into a single char array.
     * The order of elements is preserved, with elements from earlier arrays
     * placed before those of later arrays.
     *
     * @param arr a variable number of input char arrays to concatenate.
     *            Each array must not be {@code null}.
     * @return a single char array containing all elements from the provided arrays in order.
     * If no arrays are provided, an empty char array will be returned.
     * Must not be {@code null}.
     * @throws NullPointerException if {@code arr} or any of the provided arrays is null.
     */
    @NotNull
    public char[] concat(@NonNull final char[]... arr) {
        int len = 0;
        for (final char[] subArr : arr) {
            if (subArr == null) {
                throw new NullPointerException(EX_ARRAY_NULL);
            }

            len += subArr.length;
        }

        final char[] res = new char[len];

        int pos = 0;
        for (final char[] subArr : arr) {
            System.arraycopy(subArr, 0, res, pos, subArr.length);
            pos += subArr.length;
        }

        return res;
    }

    /**
     * Concatenates multiple short arrays into a single short array.
     * The order of elements is preserved, with elements from earlier arrays
     * placed before those of later arrays.
     *
     * @param arr a variable number of input short arrays to concatenate.
     *            Each array must not be {@code null}.
     * @return a single short array containing all elements from the provided arrays in order.
     * If no arrays are provided, an empty short array will be returned.
     * Must not be {@code null}.
     * @throws NullPointerException if {@code arr} or any of the provided arrays is null.
     */
    @NotNull
    public short[] concat(@NonNull final short[]... arr) {
        int len = 0;
        for (final short[] subArr : arr) {
            if (subArr == null) {
                throw new NullPointerException(EX_ARRAY_NULL);
            }

            len += subArr.length;
        }

        final short[] res = new short[len];

        int pos = 0;
        for (final short[] subArr : arr) {
            System.arraycopy(subArr, 0, res, pos, subArr.length);
            pos += subArr.length;
        }

        return res;
    }

    /**
     * Concatenates multiple boolean arrays into a single boolean array.
     * The order of elements is preserved, with elements from earlier arrays
     * placed before those of later arrays.
     *
     * @param arr a variable number of input boolean arrays to concatenate.
     *            Each array must not be {@code null}.
     * @return a single boolean array containing all elements from the provided arrays in order.
     * If no arrays are provided, an empty boolean array will be returned.
     * Must not be {@code null}.
     * @throws NullPointerException if {@code arr} or any of the provided arrays is null.
     */
    @NotNull
    public boolean[] concat(@NonNull final boolean[]... arr) {
        int len = 0;
        for (final boolean[] subArr : arr) {
            if (subArr == null) {
                throw new NullPointerException(EX_ARRAY_NULL);
            }

            len += subArr.length;
        }

        final boolean[] res = new boolean[len];

        int pos = 0;
        for (final boolean[] subArr : arr) {
            System.arraycopy(subArr, 0, res, pos, subArr.length);
            pos += subArr.length;
        }

        return res;
    }

    /**
     * Concatenates multiple float arrays into a single float array.
     * The order of elements is preserved, with elements from earlier arrays
     * placed before those of later arrays.
     *
     * @param arr a variable number of input float arrays to concatenate.
     *            Each array must not be {@code null}.
     * @return a single float array containing all elements from the provided arrays in order.
     * If no arrays are provided, an empty float array will be returned.
     * Must not be {@code null}.
     * @throws NullPointerException if {@code arr} or any of the provided arrays is null.
     */
    @NotNull
    public float[] concat(@NonNull final float[]... arr) {
        int len = 0;
        for (final float[] subArr : arr) {
            if (subArr == null) {
                throw new NullPointerException(EX_ARRAY_NULL);
            }

            len += subArr.length;
        }

        final float[] res = new float[len];

        int pos = 0;
        for (final float[] subArr : arr) {
            System.arraycopy(subArr, 0, res, pos, subArr.length);
            pos += subArr.length;
        }

        return res;
    }

    /**
     * Concatenates multiple double arrays into a single double array.
     * The order of elements is preserved, with elements from earlier arrays
     * placed before those of later arrays.
     *
     * @param arr a variable number of input double arrays to concatenate.
     *            Each array must not be {@code null}.
     * @return a single double array containing all elements from the provided arrays in order.
     * If no arrays are provided, an empty double array will be returned.
     * Must not be {@code null}.
     * @throws NullPointerException if {@code arr} or any of the provided arrays is null.
     */
    @NotNull
    public double[] concat(@NonNull final double[]... arr) {
        int len = 0;
        for (final double[] subArr : arr) {
            if (subArr == null) {
                throw new NullPointerException(EX_ARRAY_NULL);
            }

            len += subArr.length;
        }

        final double[] res = new double[len];

        int pos = 0;
        for (final double[] subArr : arr) {
            System.arraycopy(subArr, 0, res, pos, subArr.length);
            pos += subArr.length;
        }

        return res;
    }

    /**
     * Concatenates multiple long arrays into a single long array.
     * The order of elements is preserved, with elements from earlier arrays
     * placed before those of later arrays.
     *
     * @param arr a variable number of input long arrays to concatenate.
     *            Each array must not be {@code null}.
     * @return a single long array containing all elements from the provided arrays in order.
     * If no arrays are provided, an empty long array will be returned.
     * Must not be {@code null}.
     * @throws NullPointerException if {@code arr} or any of the provided arrays is null.
     */
    @NotNull
    public long[] concat(@NonNull final long[]... arr) {
        int len = 0;
        for (final long[] subArr : arr) {
            if (subArr == null) {
                throw new NullPointerException(EX_ARRAY_NULL);
            }

            len += subArr.length;
        }

        final long[] res = new long[len];

        int pos = 0;
        for (final long[] subArr : arr) {
            System.arraycopy(subArr, 0, res, pos, subArr.length);
            pos += subArr.length;
        }

        return res;
    }

    /**
     * Concatenates multiple int arrays into a single int array.
     * The order of elements is preserved, with elements from earlier arrays
     * placed before those of later arrays.
     *
     * @param arr a variable number of input int arrays to concatenate.
     *            Each array must not be {@code null}.
     * @return a single int array containing all elements from the provided arrays in order.
     * If no arrays are provided, an empty int array will be returned.
     * Must not be {@code null}.
     * @throws NullPointerException if {@code arr} or any of the provided arrays is null.
     */
    @NotNull
    public int[] concat(@NonNull final int[]... arr) {
        int len = 0;
        for (final int[] subArr : arr) {
            if (subArr == null) {
                throw new NullPointerException(EX_ARRAY_NULL);
            }

            len += subArr.length;
        }

        final int[] res = new int[len];

        int pos = 0;
        for (final int[] subArr : arr) {
            System.arraycopy(subArr, 0, res, pos, subArr.length);
            pos += subArr.length;
        }

        return res;
    }

    /**
     * Joins the elements of the provided array into a single string, with each element
     * separated by the specified delimiter.
     *
     * @param character the delimiter to be used between elements. Must not be null.
     * @param arr       the array of objects to join. Each object's {@link Object#toString()} method
     *                  will be called to get its string representation. Must not be null.
     * @return a single string containing all elements of the array, separated by the specified delimiter.
     */
    @NotNull
    public static String join(@NotNull final String character, @NotNull final Object[] arr) {
        return Arrays.stream(arr)
                .map(String::valueOf)
                .collect(Collectors.joining(character));
    }

    /**
     * Creates an {@link IntFunction} that generates arrays of the specified type.
     * The function takes the desired size of the array as input and returns a new array
     * of that size, with the specified type.
     *
     * @param <T>   the type of elements in the array
     * @param clazz the {@link Class} of the type to create arrays for
     * @return an {@link IntFunction} that generates arrays of the specified type and size
     */
    @NotNull
    public <T> IntFunction<T[]> collectToArray(@NonNull final Class<T> clazz) {
        return size -> getNewArray(clazz, size);
    }

    /**
     * Creates a new array of a specified type and size.
     *
     * @param <T>  the type of the array elements
     * @param type the {@link Class} of the type to create the array for
     * @param size the size of the new array
     * @return a new array of the specified type and size
     * @throws NullPointerException       if the {@code type} parameter is null
     * @throws NegativeArraySizeException if the {@code size} parameter is negative
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public <T> T[] getNewArray(@NonNull final Class<T> type, @Range(from = 0, to = Integer.MAX_VALUE) final int size) {
        return (T[]) Array.newInstance(type, size);
    }

    /**
     * Splits a given array into smaller subarrays of a specified size.
     * Each subarray preserves the order of elements from the original array.
     *
     * @param arr  the array to be split.
     *             must not be {@code null}.
     * @param size the size of each subarray.
     *             must be a positive integer.
     * @param <T>  the type of elements in the array.
     * @return a 2-dimensional array where each subarray contains at most {@code size} elements.
     * @throws IllegalArgumentException if {@code size} is not a positive integer.
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public <T> T[][] split(@NonNull final T[] arr, @Range(from = 1, to = Integer.MAX_VALUE) final int size) {
        if (arr == null) {
            throw new NullPointerException(EX_ARRAY_NULL);
        }
        if (0 >= size) {
            throw new IllegalArgumentException(EX_SIZE_NEGATIVE);
        }


        final Class<T> type = (Class<T>) arr.getClass().getComponentType();
        final T[][] res = getNewArray(getArrayClass(type), MathUtils.divisionWithCarry(arr.length, size));
        int pos = 0;
        int index = 0;
        while (0 < size - pos) {
            final int len = Math.min(size, arr.length - pos);

            res[index] = copyOfRange(arr, pos, len);

            index++;
            pos += len;
        }

        return res;
    }

    /**
     * Creates and returns a new array containing a specified range of elements
     * from the provided array.
     * The range is determined from the given start index and the specified length.
     *
     * @param <T>    the type of the elements in the array.
     * @param array  the original array from which elements are to be copied.
     * @param index  the starting index (inclusive) from which the copying begins.
     * @param length the number of elements to copy.
     * @return a new array containing the specified range of elements from the original array.
     * Must not be {@code null}.
     * @throws ArrayIndexOutOfBoundsException if the provided range is invalid,
     *                                        such as when the starting index is negative, or the range exceeds
     *                                        the length of the original array.
     */
    @NotNull
    public <T> T[] copyOfRange(@NonNull final T[] array, final int index, final int length) {
        if (index < 0 || array.length < index + length) {
            throw new ArrayIndexOutOfBoundsException("Invalid range");
        }

        @SuppressWarnings("unchecked") final T[] copy =
                getNewArray((Class<T>) array.getClass().getComponentType(), length);
        if (length == 0) {
            return copy;
        }

        System.arraycopy(array, index, copy, 0, length);
        return copy;
    }

    /**
     * Returns the {@link Class} object representing an array type of the specified class.
     * For example, if the provided class represents type {@link T}, this method returns
     * the {@link Class} object for {@link T[]}.
     *
     * @param <T>   the type of elements in the array
     * @param clazz the {@link Class} object representing the element type of the array;
     *              must not be {@code null}.
     * @return the {@link Class} object representing an array of the specified type.
     * Must not be {@code null}.
     * @throws NullPointerException if the {@code clazz} parameter is null
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public <T> Class<T[]> getArrayClass(final Class<T> clazz) {
        return (Class<T[]>) getNewArray(clazz, 0).getClass();
    }

    /**
     * Selects and returns a random element from the provided array.
     *
     * @param <T> the type of the elements in the array
     * @param arr an array of elements from which a random element is selected.
     *            Must not be {@code null}.
     * @return a randomly selected element from the provided array
     */
    @Nullable
    public <T> T getByRandom(@NonNull final T[] arr) {
        return arr[(int) (Math.random() * arr.length)];
    }
}
