package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class ObjectUtils {
    /**
     * Converts a byte value to its corresponding boolean representation.
     * The byte value must be either 0 or 1.
     *
     * @param bool a byte value representing a boolean, where 0 is treated as false and 1 as true.
     * @return false if the byte value is 0, true if the byte value is 1.
     * @throws IllegalArgumentException if the byte value is not 0 or 1.
     */
    public static boolean getAsBoolean(final byte bool) {
        if (bool == 0) {
            return false;
        } else if (bool == 1) {
            return true;
        }
        throw new IllegalArgumentException("Invalid boolean value " + bool);
    }


    /**
     * Converts the provided string to its corresponding boolean value.
     *
     * @param bool the string representation of a boolean value. Must be "true"
     *             (case-insensitive) to return true, or "false" (case-insensitive)
     *             to return false.
     * @return true if the provided string is "true" (case-insensitive),
     * false if the provided string is "false" (case-insensitive).
     * @throws IllegalArgumentException if the provided string is not a valid boolean value.
     */
    public static boolean getAsBoolean(@NotNull final String bool)
            throws IllegalArgumentException {
        if ("true".equalsIgnoreCase(bool)) {
            return true;
        }
        if ("false".equalsIgnoreCase(bool)) {
            return false;
        }
        throw new IllegalArgumentException("Invalid boolean value " + bool);
    }

    /**
     * Converts a {@link Boolean} value to its corresponding {@link Byte} representation.
     * If the boolean value is true, it returns 1. If the value is false, it returns 0.
     * If the input is null, it returns null.
     *
     * @param bool the {@link Boolean} value to be converted, which can be null.
     * @return the {@link Byte} representation of the boolean value, or null if the input is null.
     */
    @Nullable
    public static Byte toByte(@Nullable final Boolean bool) {
        return bool != null ? (byte) (bool ? 1 : 0) : null;
    }
}
