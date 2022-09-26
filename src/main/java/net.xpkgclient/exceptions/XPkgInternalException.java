package net.xpkgclient.exceptions;

import java.io.Serial;

// Exceptions meant to be caught in order to be thrown later (useful for full
// stack trace debugging), used mainly in static classes and passing
// information
public class XPkgInternalException extends Exception {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -6371838915978668117L;

    Object data;

    // Basic constructors
    public XPkgInternalException() {
        this(null, null);
    }

    public XPkgInternalException(String message) {
        this(message, null);
    }

    public XPkgInternalException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public XPkgInternalException(String message, Exception cause) {
        super(message, cause);
    }

    // We want to call our own constructor to set this.message
    public XPkgInternalException(Exception cause) {
        this(cause == null ? null : cause.toString(), cause);
        if (cause instanceof XPkgInternalException)
            data = ((XPkgInternalException) cause).getData();
    }

    // Get the data passed in
    public Object getData() {
        return data;
    }
}
