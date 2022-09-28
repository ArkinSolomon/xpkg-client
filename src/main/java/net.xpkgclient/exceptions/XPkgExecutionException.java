package net.xpkgclient.exceptions;

import java.io.Serial;

// Top level exception for exceptions that are not the script programmers fault, but rather an error with the OS or XPkg
public class XPkgExecutionException extends XPkgScriptRunnerException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = 3113973855303520458L;

    // Class constructors (same as superclass's)
    public XPkgExecutionException() {
    }

    public XPkgExecutionException(String message) {
        super(message);
    }

    public XPkgExecutionException(String message, Exception cause) {
        super(message, cause);
    }

    public XPkgExecutionException(Exception cause) {
        super(cause);
    }
}
