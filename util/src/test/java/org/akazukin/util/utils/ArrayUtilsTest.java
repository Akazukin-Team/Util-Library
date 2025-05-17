package org.akazukin.util.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayUtilsTest {
    @Test
    public void testConcatWithNoArrays() {
        final String[] arr = {};
        final String[] res = ArrayUtils.concat(arr);
        assert res.length == 0;
    }

    @Test
    public void testConcatWithNullArrays() {
        final String[] a = {"a", "b"};
        final String[] nullArray = null;

        Assertions.assertThrows(NullPointerException.class,
                () -> ArrayUtils.concat(a, nullArray));
    }

    @Test
    public void testConcatWithMixedContent() {
        final String[] a = {"1", "2", "3"};
        final String[] b = {};
        final String[] c = {"4", null};

        final String[] res = ArrayUtils.concat(a, b, c);
        assert res.length == 5;
        assert res[0].equals("1");
        assert res[1].equals("2");
        assert res[2].equals("3");
        assert res[3].equals("4");
        assert res[4] == null;
    }

    @Test
    public void testConcatWithNullByteArrays() {
        final byte[] a = {1, 2};
        final byte[] nullArray = null;

        Assertions.assertThrows(NullPointerException.class,
                () -> ArrayUtils.concat(a, nullArray));
    }

    @Test
    public void testConcatByteArrays() {
        final byte[] a = {1, 2};
        final byte[] b = {3};
        final byte[] c = {};
        final byte[] d = {4, 5, 6};

        final byte[] res = ArrayUtils.concat(a, b, c, d);

        assert res.length == 6;
        assert res[0] == 1;
        assert res[1] == 2;
        assert res[2] == 3;
        assert res[3] == 4;
        assert res[4] == 5;
        assert res[5] == 6;
    }

    @Test
    public void testConcatShortArrays() {
        final short[] a = {1, 2};
        final short[] b = {3};
        final short[] c = {};
        final short[] d = {4, 5, 6};

        final short[] res = ArrayUtils.concat(a, b, c, d);

        assert res.length == 6;
        assert res[0] == 1;
        assert res[1] == 2;
        assert res[2] == 3;
        assert res[3] == 4;
        assert res[4] == 5;
        assert res[5] == 6;
    }

    @Test
    public void testConcatCharArrays() {
        final char[] a = {1, 2};
        final char[] b = {3};
        final char[] c = {};
        final char[] d = {4, 5, 6};

        final char[] res = ArrayUtils.concat(a, b, c, d);

        assert res.length == 6;
        assert res[0] == 1;
        assert res[1] == 2;
        assert res[2] == 3;
        assert res[3] == 4;
        assert res[4] == 5;
        assert res[5] == 6;
    }

    @Test
    public void testConcatBooleanArrays() {
        final boolean[] a = {true, false};
        final boolean[] b = {true};
        final boolean[] c = {};
        final boolean[] d = {true, false, true};

        final boolean[] res = ArrayUtils.concat(a, b, c, d);

        assert res.length == 6;
        assert res[0];
        assert !res[1];
        assert res[2];
        assert res[3];
        assert !res[4];
        assert res[5];
    }

    @Test
    public void testConcatFloatArrays() {
        final float[] a = {1, 2};
        final float[] b = {3};
        final float[] c = {};
        final float[] d = {4, 5, 6};

        final float[] res = ArrayUtils.concat(a, b, c, d);

        assert res.length == 6;
        assert res[0] == 1;
        assert res[1] == 2;
        assert res[2] == 3;
        assert res[3] == 4;
        assert res[4] == 5;
        assert res[5] == 6;
    }

    @Test
    public void testConcatDoubleArrays() {
        final double[] a = {1, 2};
        final double[] b = {3};
        final double[] c = {};
        final double[] d = {4, 5, 6};

        final double[] res = ArrayUtils.concat(a, b, c, d);

        assert res.length == 6;
        assert res[0] == 1;
        assert res[1] == 2;
        assert res[2] == 3;
        assert res[3] == 4;
        assert res[4] == 5;
        assert res[5] == 6;
    }

    @Test
    public void testConcatIntArrays() {
        final int[] a = {1, 2};
        final int[] b = {3};
        final int[] c = {};
        final int[] d = {4, 5, 6};

        final int[] res = ArrayUtils.concat(a, b, c, d);

        assert res.length == 6;
        assert res[0] == 1;
        assert res[1] == 2;
        assert res[2] == 3;
        assert res[3] == 4;
        assert res[4] == 5;
        assert res[5] == 6;
    }

    @Test
    public void testConcatLongArrays() {
        final long[] a = {1, 2};
        final long[] b = {3};
        final long[] c = {};
        final long[] d = {4, 5, 6};

        final long[] res = ArrayUtils.concat(a, b, c, d);

        assert res.length == 6;
        assert res[0] == 1;
        assert res[1] == 2;
        assert res[2] == 3;
        assert res[3] == 4;
        assert res[4] == 5;
        assert res[5] == 6;
    }
}
