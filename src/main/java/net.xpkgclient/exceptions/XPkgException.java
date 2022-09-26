package net.xpkgclient.exceptions;

import java.io.Serial;

//Top level exception for all custom XPkgExceptions
public class XPkgException extends Exception {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = 5674756055664564736L;

    // Basic constructors (basically the same as Exception)
    public XPkgException() {
        this((String) null);
    }

    public XPkgException(String message) {
        super(message);
    }

    public XPkgException(String message, Exception cause) {
        super(message, cause);
    }

    public XPkgException(Exception cause) {
        super(cause);
    }
}