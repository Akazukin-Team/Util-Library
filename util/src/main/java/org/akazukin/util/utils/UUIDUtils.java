package org.akazukin.util.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility class for handling operations related to UUIDs (Universally Unique Identifiers).
 * This class provides methods to validate UUID formats, validate UUID versions,
 * and convert strings to UUID objects.
 */
@UtilityClass
public class UUIDUtils {
    public final static String UUID_VERSION_EXCEPTION = "The UUID version is not supported";

    private final static Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9A-F]{8}-" +
                    "[0-9A-F]{4}-" +
                    "[1-7][0-9A-F]{3}-" +
                    "[89AB][0-9A-F]{3}-" +
                    "[0-9A-F]{12}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern UUID_V1_PATTERN = Pattern.compile(
            "^[0-9a-f]{8}-" +
                    "[0-9a-f]{4}-" +
                    "1[0-9a-f]{3}-" + // Version 1
                    "[89ab][0-9a-f]{3}-" +
                    "[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern UUID_V2_PATTERN = Pattern.compile(
            "^[0-9a-f]{8}-" +
                    "[0-9a-f]{4}-" +
                    "2[0-9a-f]{3}-" + // Version 2
                    "[89ab][0-9a-f]{3}-" +
                    "[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern UUID_V3_PATTERN = Pattern.compile(
            "^[0-9a-f]{8}-" +
                    "[0-9a-f]{4}-" +
                    "3[0-9a-f]{3}-" + // Version 3
                    "[89ab][0-9a-f]{3}-" +
                    "[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern UUID_V4_PATTERN = Pattern.compile(
            "^[0-9a-f]{8}-" +
                    "[0-9a-f]{4}-" +
                    "4[0-9a-f]{3}-" + // Version 4
                    "[89ab][0-9a-f]{3}-" +
                    "[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern UUID_V5_PATTERN = Pattern.compile(
            "^[0-9a-f]{8}-" +
                    "[0-9a-f]{4}-" +
                    "5[0-9a-f]{3}-" + // Version 5
                    "[89ab][0-9a-f]{3}-" +
                    "[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern UUID_V6_PATTERN = Pattern.compile(
            "^[0-9a-f]{8}-" +
                    "[0-9a-f]{4}-" +
                    "6[0-9a-f]{3}-" + // Version 6
                    "[89ab][0-9a-f]{3}-" +
                    "[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern UUID_V7_PATTERN = Pattern.compile(
            "^[0-9a-f]{8}-" +
                    "[0-9a-f]{4}-" +
                    "7[0-9a-f]{3}-" + // Version 7
                    "[89ab][0-9a-f]{3}-" +
                    "[0-9a-f]{12}$", Pattern.CASE_INSENSITIVE);

    /**
     * Validates whether the provided string is a valid UUID.
     *
     * @param uuid the string to validate against the UUID pattern. Must not be null.
     * @return {@code true} if the provided string matches the UUID pattern;
     * {@code false} otherwise.
     */
    public static boolean isUUID(final String uuid) {
        return UUIDUtils.UUID_PATTERN.matcher(uuid).matches();
    }

    /**
     * Validates whether the given string represents a UUID of a specific version.
     *
     * @param uuid    the UUID string to validate. Must not be null.
     * @param version the expected version of the UUID (1 through 7).
     * @return {@code true} if the string matches the pattern of the specified UUID version;
     * {@code false} otherwise.
     * @throws UnsupportedOperationException if the specified version is not supported.
     */
    public static boolean isUUID(final String uuid, final int version) {
        if (version == 1) {
            return UUIDUtils.UUID_V1_PATTERN.matcher(uuid).matches();
        } else if (version == 2) {
            return UUIDUtils.UUID_V2_PATTERN.matcher(uuid).matches();
        } else if (version == 3) {
            return UUIDUtils.UUID_V3_PATTERN.matcher(uuid).matches();
        } else if (version == 4) {
            return UUIDUtils.UUID_V4_PATTERN.matcher(uuid).matches();
        } else if (version == 5) {
            return UUIDUtils.UUID_V5_PATTERN.matcher(uuid).matches();
        } else if (version == 6) {
            return UUIDUtils.UUID_V6_PATTERN.matcher(uuid).matches();
        } else if (version == 7) {
            return UUIDUtils.UUID_V7_PATTERN.matcher(uuid).matches();
        } else {
            throw new UnsupportedOperationException(UUID_VERSION_EXCEPTION);
        }
    }

    /**
     * Converts the given string to a {@link UUID}.
     *
     * @param str the string to convert to a UUID. This can be null, in which case the method
     *            will return null.
     * @return the {@link UUID} representation of the input string if it is a valid UUID string
     * format; null if the input string is null.
     * @throws IllegalArgumentException if the string is not a valid UUID format.
     */
    @Nullable
    public static UUID toUuid(@Nullable final String str) {
        if (str == null) {
            return null;
        }
        return UUID.fromString(str);
    }
}
