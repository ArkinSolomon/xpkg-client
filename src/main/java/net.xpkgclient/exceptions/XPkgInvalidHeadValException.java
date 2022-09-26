package net.xpkgclient.exceptions;

import net.xpkgclient.enums.HeadKey;

import java.io.Serial;

// Exception thrown if a value is not suitable for a head key
public class XPkgInvalidHeadValException extends XPkgParseException {

    //Serial identifier
    @Serial
    private static final long serialVersionUID = 6072185362526976813L;

    // Basic constructor to show a simple message
    public XPkgInvalidHeadValException(HeadKey hk, String invalidVal) {
        super("Invalid head value: '" + invalidVal + "' is not a valid value for " + hk);
    }
}
