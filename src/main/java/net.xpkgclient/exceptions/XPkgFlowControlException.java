package net.xpkgclient.exceptions;

import java.io.Serial;

// Exceptions thrown with flow control errors
public class XPkgFlowControlException extends XPkgParseException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = 2081401572143123366L;

    public XPkgFlowControlException(String message) {
        super("Flow control error: " + message);
    }
}
