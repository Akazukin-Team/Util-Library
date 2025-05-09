package org.akazukin.util.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ListUtilsTest {

    @Test
    void testListCopyWithValidInputs() {
        final List<Integer> source = Arrays.asList(1, 2, 3, 4, 5);
        final List<Integer> destination = Arrays.asList(null, null, null, null, null);

        ListUtils.listCopy(source, 1, destination, 2, 3);

        assertEquals(Arrays.asList(null, null, 2, 3, 4), destination);
    }

    @Test
    void testListCopyWithNegativeSrcPos() {
        final List<Integer> source = Arrays.asList(1, 2, 3);
        final List<Integer> destination = new ArrayList<>();

        assertThrows(IndexOutOfBoundsException.class, () ->
                ListUtils.listCopy(source, -1, destination, 0, 1));
    }

    @Test
    void testListCopyWithNegativeDestPos() {
        final List<Integer> source = Arrays.asList(1, 2, 3);
        final List<Integer> destination = new ArrayList<>();

        assertThrows(IndexOutOfBoundsException.class, () ->
                ListUtils.listCopy(source, 0, destination, -1, 1));
    }

    @Test
    void testListCopyWithNegativeLength() {
        final List<Integer> source = Arrays.asList(1, 2, 3);
        final List<Integer> destination = new ArrayList<>();

        assertThrows(IndexOutOfBoundsException.class, () ->
                ListUtils.listCopy(source, 0, destination, 0, -1));
    }

    @Test
    void testListCopyWithSrcPosAndLengthExceedingSrcSize() {
        final List<Integer> source = Arrays.asList(1, 2, 3);
        final List<Integer> destination = new ArrayList<>(Arrays.asList(null, null, null));

        assertThrows(IndexOutOfBoundsException.class, () ->
                ListUtils.listCopy(source, 2, destination, 0, 2));
    }

    @Test
    void testListCopyWithDestPosAndLengthExceedingDestSize() {
        final List<Integer> source = Arrays.asList(1, 2, 3);
        final List<Integer> destination = new ArrayList<>(Arrays.asList(null, null, null));

        assertThrows(IndexOutOfBoundsException.class, () ->
                ListUtils.listCopy(source, 0, destination, 2, 2));
    }

    @Test
    void testSplitWithExactDivision() {
        final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);

        final List<Integer>[] result = ListUtils.split(list, 2);

        assertEquals(3, result.length);
        assertEquals(Arrays.asList(1, 2), result[0]);
        assertEquals(Arrays.asList(3, 4), result[1]);
        assertEquals(Arrays.asList(5, 6), result[2]);
    }

    @Test
    void testSplitWithRemainingElements() {
        final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        final List<Integer>[] result = ListUtils.split(list, 2);

        assertEquals(3, result.length);
        assertEquals(Arrays.asList(1, 2), result[0]);
        assertEquals(Arrays.asList(3, 4), result[1]);
        assertEquals(Collections.singletonList(5), result[2]);
    }

    @Test
    void testSplitBySizeGreaterThanList() {
        final List<Integer> list = Arrays.asList(1, 2, 3);

        final List<Integer>[] result = ListUtils.split(list, 5);

        assertEquals(1, result.length);
        assertEquals(Arrays.asList(1, 2, 3), result[0]);
    }

    @Test
    void testSplitWithEmptyList() {
        final List<Integer> list = Collections.emptyList();

        final List<Integer>[] result = ListUtils.split(list, 2);

        assertEquals(0, result.length);
    }

    @Test
    void testSplitByNegativeSize() {
        final List<Integer> list = Arrays.asList(1, 2, 3);

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> ListUtils.split(list, -1));

        assertEquals(ListUtils.SIZE_NOT_POSITIVE, exception.getMessage());
    }

    @Test
    void testSplitByZeroSize() {
        final List<Integer> list = Arrays.asList(1, 2, 3);

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> ListUtils.split(list, 0));

        assertEquals(ListUtils.SIZE_NOT_POSITIVE, exception.getMessage());
    }
}