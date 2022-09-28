package net.xpkgclient.exceptions;

import java.io.Serial;

// Exception thrown when a boolean statement is invalid
public class XPkgInvalidBoolStatement extends XPkgParseException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = 3542483398964626556L;

    //When there is an invalid item
    public XPkgInvalidBoolStatement(String item) {
        super("Invalid item in boolean statement: '" + item + "'");
    }
}
