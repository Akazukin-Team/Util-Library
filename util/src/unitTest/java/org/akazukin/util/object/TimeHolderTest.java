package org.akazukin.util.object;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class TimeHolderTest {
    @Test
    void testAddTimeWithSameUnit() {
        final TimeHolder timeHolder = new TimeHolder(TimeUnit.SECONDS);
        timeHolder.addTime(5, TimeUnit.SECONDS);
        assertEquals(5, timeHolder.toConvert(TimeUnit.SECONDS));
    }

    @Test
    void testAddTimeWithDifferentUnitToSec() {
        final TimeHolder timeHolder = new TimeHolder(TimeUnit.SECONDS);
        timeHolder.addTime(1, TimeUnit.MINUTES);
        assertEquals(60, timeHolder.toConvert(TimeUnit.SECONDS));
    }

    @Test
    void testAddTimeWithNsToMs() {
        final TimeHolder timeHolder = new TimeHolder(TimeUnit.MILLISECONDS);
        timeHolder.addTime(1000_000, TimeUnit.NANOSECONDS);
        assertEquals(1, timeHolder.toConvert(TimeUnit.MILLISECONDS));
    }

    @Test
    void testAddTimeWithMultipleUnitsToNs() {
        final TimeHolder timeHolder = new TimeHolder(TimeUnit.NANOSECONDS);
        timeHolder.addTime(5, TimeUnit.SECONDS);
        timeHolder.addTime(500, TimeUnit.MILLISECONDS);
        assertEquals(5_500_000_000L, timeHolder.toConvert(TimeUnit.NANOSECONDS));
    }

    @Test
    void testAddTimeWithZero() {
        final TimeHolder timeHolder = new TimeHolder(TimeUnit.SECONDS);
        timeHolder.addTime(0, TimeUnit.SECONDS);
        assertEquals(0, timeHolder.toConvert(TimeUnit.SECONDS));
    }

    @Test
    void testAddTimeWithNegativeValue() {
        final TimeHolder timeHolder = new TimeHolder(TimeUnit.SECONDS);
        timeHolder.addTime(-30, TimeUnit.SECONDS);
        assertEquals(-30, timeHolder.toConvert(TimeUnit.SECONDS));
    }
}