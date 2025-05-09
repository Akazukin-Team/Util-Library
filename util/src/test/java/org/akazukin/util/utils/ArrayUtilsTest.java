package org.akazukin.util.utils;

import org.junit.jupiter.api.Test;

public class ArrayUtilsTest {
    @Test
    public void testConcat() {
        final String[] a = {"a", "b", "c"};
        final String[] b = {"d", "e"};
        final String[] c = {};
        final String[] d = {"f", "g", "h"};

        final String[] res = ArrayUtils.concat(a, b, c, d);

        assert res.length == 8;
        assert res[0].equals("a");
        assert res[1].equals("b");
        assert res[2].equals("c");
        assert res[3].equals("d");
        assert res[4].equals("e");
        assert res[5].equals("f");
        assert res[6].equals("g");
        assert res[7].equals("h");
    }
}
