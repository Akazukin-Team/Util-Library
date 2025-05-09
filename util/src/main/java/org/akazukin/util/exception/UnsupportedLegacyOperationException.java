package org.akazukin.util.exception;

/**
 * Thrown to indicate that an operation is not supported within the current legacy context.
 * <p>
 * This exception is typically used to signal that a specific operation cannot be performed
 * because it relies on legacy functionality or systems that are no longer supported.
 * It extends {@link UnsupportedOperationException} to provide more specific semantic context for legacy-related operations.
 * <p>
 * Typical usage includes marking optional or deleted functionalities
 * that be invoked in a legacy context with implementation.
 *
 * @see UnsupportedOperationException
 */
public class UnsupportedLegacyOperationException extends UnsupportedOperationException {
    private static final long serialVersionUID = -923753289532491726L;

    public UnsupportedLegacyOperationException() {
    }

    public UnsupportedLegacyOperationException(final String message) {
        super(message);
    }

    public UnsupportedLegacyOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnsupportedLegacyOperationException(final Throwable cause) {
        super(cause);
    }
}
