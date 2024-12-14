package org.akazukin.util.exception;

public class UnsupportedOperationYetException extends UnsupportedOperationException {
    private static final long serialVersionUID = -923753289532491726L;

    public UnsupportedOperationYetException() {
    }

    public UnsupportedOperationYetException(final String message) {
        super(message);
    }

    public UnsupportedOperationYetException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnsupportedOperationYetException(final Throwable cause) {
        super(cause);
    }
}
