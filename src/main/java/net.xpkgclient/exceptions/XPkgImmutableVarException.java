package net.xpkgclient.exceptions;

import java.io.Serial;

// Exception when trying to change an immutable variable
public class XPkgImmutableVarException extends XPkgRuntimeException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -5842352310837071330L;

    // Say that the variable is immutable
    public XPkgImmutableVarException(String varName) {
        super("'" + varName + "' is immutable and can not be changed");
    }
}
