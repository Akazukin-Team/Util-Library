package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

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
    /**
     * Concatenate multiple arrays into one.
     *
     * @param arrays the arrays to concatenate
     * @param <T>    the type of the elements in the arrays
     * @return the concatenated array
     */
    @SuppressWarnings("unchecked")
    public <T> T[] concat(final T[]... arrays) {
        final Class<T> type1 = (Class<T>) arrays.getClass().getComponentType().getComponentType();

        int len = 0;
        for (final T[] ts : arrays) {
            len += ts.length;
        }

        final T[] res = (T[]) Array.newInstance(type1, len);

        int pos = 0;
        for (final T[] ts : arrays) {
            System.arraycopy(ts, 0, res, pos, ts.length);
            pos += ts.length;
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
    public <T> IntFunction<T[]> collectToArray(final Class<T> clazz) {
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
    public <T> T[] getNewArray(final Class<T> type, final int size) {
        return (T[]) Array.newInstance(type, size);
    }

    /**
     * Splits a given array into smaller subarrays of a specified size.
     * Each subarray preserves the order of elements from the original array.
     *
     * @param array the array to be split; must not be null
     * @param size  the size of each subarray; must be a positive integer
     * @param <T>   the type of elements in the array
     * @return a 2-dimensional array where each subarray contains at most {@code size} elements
     * @throws IllegalArgumentException if {@code size} is not a positive integer
     */
    @SuppressWarnings("unchecked")
    public <T> T[][] split(@NotNull final T[] array, final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException(ListUtils.SIZE_NOT_POSITIVE);
        }

        final Class<T> type = (Class<T>) array.getClass().getComponentType();
        final T[][] arr = getNewArray(getArrayClass(type), MathUtils.divisionWithCarry(array.length, size));
        int pos = 0;
        int index = 0;
        while (size - pos > 0) {
            final int len = Math.min(size, array.length - pos);

            arr[index] = copyOfRange(array, pos, len);

            index++;
            pos += len;
        }

        return arr;
    }

    /**
     * Creates and returns a new array containing a specified range of elements
     * from the provided array.
     * The range is determined from the given start index and the specified length.
     *
     * @param <T>    the type of the elements in the array
     * @param array  the original array from which elements are to be copied
     * @param index  the starting index (inclusive) from which the copying begins
     * @param length the number of elements to copy
     * @return a new array containing the specified range of elements from the original array
     * @throws ArrayIndexOutOfBoundsException if the provided range is invalid,
     *                                        such as when the starting index is negative, or the range exceeds
     *                                        the length of the original array
     */
    public <T> T[] copyOfRange(final T[] array, final int index, final int length) {
        if (index < 0 || index + length > array.length) {
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
     * For example, if the provided class represents type {@code T}, this method returns
     * the {@link Class} object for {@code T[]}.
     *
     * @param <T>   the type of elements in the array
     * @param clazz the {@link Class} object representing the element type of the array;
     *              must not be null
     * @return the {@link Class} object representing an array of the specified type
     * @throws NullPointerException if the {@code clazz} parameter is null
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T[]> getArrayClass(final Class<T> clazz) {
        return (Class<T[]>) getNewArray(clazz, 0).getClass();
    }

    /**
     * Selects and returns a random element from the provided array.
     *
     * @param <T>  the type of the elements in the array
     * @param list an array of elements from which a random element is selected; must not be null
     * @return a randomly selected element from the provided array
     */
    public <T> T getByRandom(@NotNull final T[] list) {
        return list[((Double) (Math.random() * list.length)).intValue()];
    }
}
