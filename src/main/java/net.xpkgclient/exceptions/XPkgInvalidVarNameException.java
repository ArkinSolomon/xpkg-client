package net.xpkgclient.exceptions;

import java.io.Serial;

// Exception thrown when a variable name is invalid
public class XPkgInvalidVarNameException extends XPkgParseException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -2027953150186197578L;

    // Basic constructor
    public XPkgInvalidVarNameException(String name) {
        super("Invalid variable name: '" + name + "' is not a valid variable");
    }
}
