package org.akazukin.util.exception;

public class UnsupportedOperationAlreadyException extends UnsupportedOperationException {
    private static final long serialVersionUID = -923753289532491726L;

    public UnsupportedOperationAlreadyException() {
    }

    public UnsupportedOperationAlreadyException(final String message) {
        super(message);
    }

    public UnsupportedOperationAlreadyException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnsupportedOperationAlreadyException(final Throwable cause) {
        super(cause);
    }
}
