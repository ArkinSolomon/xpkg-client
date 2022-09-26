package net.xpkgclient.exceptions;

import java.io.Serial;

// A simple exception when an item can not be set
public class XPkgAlreadySetException extends XPkgInvalidCallException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = 7960672666236728584L;

    public XPkgAlreadySetException(String var) {
        super("'" + var + "' has already been set");
    }
}
