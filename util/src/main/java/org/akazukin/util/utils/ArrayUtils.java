package org.akazukin.util.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.IntFunction;

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

    @SuppressWarnings("unchecked")
    public <T> T[] fromList(final List<T> list) {
        return list.toArray((T[]) Array.newInstance(list.get(0).getClass(), list.size()));
    }

    @SuppressWarnings("unchecked")
    public <T> IntFunction<T[]> collectToArray(final Class<T> clazz) {
        return size -> (T[]) Array.newInstance(clazz, size);
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
    public <T> T[][] split(@NonNull final T[] array, final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException(ListUtils.SIZE_NOT_POSITIVE);
        }

        final Class<T> type = (Class<T>) array.getClass().getComponentType();
        final T[][] arr = (T[][]) Array.newInstance(getArrayClass(type), MathUtils.divisionWithCarry(array.length, size));
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

        final T[] copy = (T[]) Array.newInstance(array.getClass().getComponentType(), length);
        if (length == 0) {
            return copy;
        }

        System.arraycopy(array, index, copy, 0, length);
        return copy;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T[]> getArrayClass(final Class<T> clazz) {
        return (Class<T[]>) Array.newInstance(clazz, 0).getClass();
    }
}
