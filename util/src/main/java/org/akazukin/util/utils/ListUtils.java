package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ListUtils {
    public static final List[] EMPTY_LIST = {};
    public static final String SIZE_NOT_POSITIVE = "size must be positive";

    public static final String SRC_POS_IS_NEGATIVE = "srcPos is negative";
    public static final String DEST_POS_IS_NEGATIVE = "destPos is negative";
    public static final String LENGTH_IS_NEGATIVE = "length is negative";

    /**
     * Splits the given list into smaller sublists of the specified size.
     * Each sublist will contain elements from the original list, preserving the order.
     *
     * @param list the list to be split; must not be null
     * @param size the size of each sublist; must be a positive integer
     * @param <T>  the type of elements in the list
     * @return an array of sublists, where each sublist contains at most {@code size} elements
     * @throws IllegalArgumentException if {@code size} is not a positive integer
     */
    @SuppressWarnings("unchecked")
    public <T> List<T>[] split(@NotNull final List<T> list, final int size) {
        if (size <= 0) {
            throw new IllegalArgumentException(ListUtils.SIZE_NOT_POSITIVE);
        }

        if (list.isEmpty()) {
            return EMPTY_LIST;
        }

        final Class<T> type = (Class<T>) list.get(0).getClass();
        final List<T>[] arr = (List<T>[]) Array.newInstance(
                list.getClass(),
                MathUtils.divisionWithCarry(list.size(), size));

        int pos = 0;
        int index = 0;
        while (list.size() - pos > 0) {
            final int len = Math.min(size, list.size() - pos);

            final List<T> sub = Arrays.asList((T[]) Array.newInstance(type, len));
            listCopy(list, pos, sub, 0, len);
            arr[index] = sub;

            index++;
            pos += len;
        }

        return arr;
    }

    /**
     * Copies elements from a source list to a destination list. The method allows
     * specifying the starting position and the number of elements to copy for both
     * the source and the destination lists.
     *
     * @param src     the source object to copy elements from, must be a {@code List}
     * @param srcPos  the starting position in the source list
     * @param dest    the destination object to copy elements to, must be a {@code List}
     * @param destPos the starting position in the destination list
     * @param length  the number of elements to copy
     * @throws IndexOutOfBoundsException if {@code srcPos}, {@code destPos}, or {@code length} is negative,
     *                                   or if the range specified by {@code srcPos} and {@code length} exceeds
     *                                   the size of the source list, or if the range specified by {@code destPos}
     *                                   and {@code length} exceeds the size of the destination list
     * @throws ClassCastException        if either {@code src} or {@code dest} is not a {@code List}
     */
    public void listCopy(final Object src, final int srcPos, final Object dest, final int destPos, final int length) throws IndexOutOfBoundsException, ClassCastException {
        if (srcPos < 0) {
            throw new IndexOutOfBoundsException(SRC_POS_IS_NEGATIVE);
        }
        if (destPos < 0) {
            throw new IndexOutOfBoundsException(DEST_POS_IS_NEGATIVE);
        }
        if (length < 0) {
            throw new IndexOutOfBoundsException(LENGTH_IS_NEGATIVE);
        }

        final List listSrc = (List) src;
        final List listDest = (List) dest;
        if (srcPos + length > listSrc.size()) {
            throw new IndexOutOfBoundsException("The sum of srcPos and length is greater than src length");
        }
        if (destPos + length > listDest.size()) {
            throw new IndexOutOfBoundsException("The sum of destPos and length is greater than dest length");
        }


        for (int i = 0; i < length; i++) {
            listDest.set(destPos + i, listSrc.get(srcPos + i));
        }
    }

    /**
     * Joins the elements of the provided list into a single string, with each element
     * separated by the specified delimiter.
     *
     * @param character the delimiter to be used between elements. Must not be null.
     * @param list      the list of objects to join. Each object's {@link Object#toString()} method
     *                  will be called to get its string representation. Must not be null.
     * @return a single string containing all elements of the list, separated by the specified delimiter.
     */
    @NotNull
    public String join(@NotNull final String character, @NotNull final Collection<?> list) {
        return list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(character));
    }

    /**
     * Selects and returns a random element from the provided list.
     *
     * @param <T>  the type of the elements in the list
     * @param list a list of elements from which a random element is selected; must not be null
     * @return a randomly selected element from the provided list
     */
    public <T> T getByRandom(@NotNull final List<T> list) {
        return list.get(((Double) (Math.random() * list.size())).intValue());
    }
}
