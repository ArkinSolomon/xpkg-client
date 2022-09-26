package net.xpkgclient.exceptions;

import java.io.Serial;

// An exception thrown if a head key is invalid
public class XPkgInvalidHeadKeyException extends XPkgParseException {

    //Serial identifier
    @Serial
    private static final long serialVersionUID = -6211878735670435376L;

    // Basic constructor to show a simple message
    public XPkgInvalidHeadKeyException(String invalidKey) {
        super("Invalid head key: '" + invalidKey + "' is not a valid head key");
    }
}
