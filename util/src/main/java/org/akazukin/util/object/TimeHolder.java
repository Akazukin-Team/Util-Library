package org.akazukin.util.object;

import org.akazukin.annotation.marker.NonThreadSafe;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Represents a utility to hold and manipulate time values with a specified {@link TimeUnit}.
 * <p>
 * This class provides methods to store, update, and convert time values
 * between different units. The time is internally held in the unit specified
 * during the instantiation of the object.
 */
@NonThreadSafe
public class TimeHolder {
    /**
     * The time unit used as the internal representation for time values.
     * <p>
     * This field determines the unit of measurement for all internally stored
     * time and conversion operations within the containing {@code TimeHolder} class.
     * It is initialized during the object construction and remains constant
     * throughout the lifecycle of the instance.
     *
     * @see TimeUnit
     */
    private final TimeUnit unit;
    /**
     * Stores the current time value in the internal {@link TimeUnit} of the {@code TimeHolder}.
     * <p>
     * This field holds the raw time data in the unit defined by the {@code unit} field of
     * the containing {@code TimeHolder} class. All operations related to time addition
     * or conversion interact with this field as the core storage.
     */
    private long time;

    /**
     * Constructs a {@code TimeHolder} instance with the default {@link TimeUnit#NANOSECONDS}.
     * <p>
     * This no-argument constructor initializes the {@code TimeHolder} to use
     * nanoseconds as its internal time unit.
     */
    public TimeHolder() {
        this(TimeUnit.NANOSECONDS);
    }

    /**
     * Constructs a {@code TimeHolder} instance with the specified {@link TimeUnit}.
     * The internally stored time values will be based on the provided {@code TimeUnit}.
     *
     * @param unit the {@link TimeUnit} to be used as the internal unit of time.
     *             Must not be {@code null}.
     */
    public TimeHolder(@NotNull final TimeUnit unit) {
        this.unit = unit;
    }

    /**
     * Adds the specified amount of time to the current time, converting the given time
     * value from the specified {@link TimeUnit} to the internal time unit.
     *
     * @param time the amount of time to add, in the specified {@code TimeUnit}.
     * @param unit the unit of the time being added.
     *             Must not be {@code null}.
     */
    public void addTime(final long time, @NotNull final TimeUnit unit) {
        this.time += this.unit.convert(time, unit);
    }

    /**
     * Converts the internally stored time to the specified time unit.
     * <p>
     * This method uses the {@link TimeUnit#convert(long, TimeUnit)} method to
     * transform the stored time into the provided {@code TimeUnit}.
     *
     * @param unit the {@link TimeUnit} to which the internal time should be converted.
     *             Must not be {@code null}.
     * @return the time converted to the specified {@code TimeUnit}.
     */
    public long toConvert(@NotNull final TimeUnit unit) {
        return unit.convert(this.time, this.unit);
    }
}
