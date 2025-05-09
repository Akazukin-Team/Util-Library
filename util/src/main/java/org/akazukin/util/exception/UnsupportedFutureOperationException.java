package org.akazukin.util.exception;

import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * Thrown to indicate that an operation is not currently supported, as it pertains
 * to functionality that is planned or intended to be added in the future.
 * <p>
 * This exception serves as a placeholder to document features, methods, or
 * operations that are not yet implemented but are expected to be introduced in upcoming versions.
 * It extends {@link UnsupportedOperationException} to provide more specific semantic context for future-related operations.
 * <p>
 * Typical usage includes marking optional or planned functionalities that may be invoked prematurely in a development
 * context without actual implementation.
 *
 * @see UnsupportedOperationException
 */
public class UnsupportedFutureOperationException extends UnsupportedOperationException {
    private static final long serialVersionUID = -923753289532491726L;

    Version version;

    public UnsupportedFutureOperationException() {
    }

    public UnsupportedFutureOperationException(final String message) {
        super(message);
    }

    public UnsupportedFutureOperationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnsupportedFutureOperationException(final Throwable cause) {
        super(cause);
    }

    public UnsupportedFutureOperationException(final String message, final Version version) {
        super(message);
    }

    public UnsupportedFutureOperationException(final String message, final Version version, final Throwable cause) {
        super(message, cause);
    }

    public UnsupportedFutureOperationException(final Version version, final Throwable cause) {
        super(cause);
    }

    @Override
    public String toString() {
        return super.toString() + (this.version != null ? " (version: " + this.version.getVersion() + ")" : "");
    }

    @AllArgsConstructor(staticName = "of")
    public static class Version implements Serializable {
        public static final long serialVersionUID = 1L;

        String versionStr;
        long versionNum;

        public String getVersion() {
            return this.versionStr != null ? this.versionStr : String.valueOf(this.versionNum);
        }
    }
}
