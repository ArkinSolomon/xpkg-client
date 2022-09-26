package net.xpkgclient.exceptions;

import java.io.Serial;

//Exception thrown when a variable is undefined
public class XPkgUndefinedVarException extends XPkgRuntimeException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -2122679038771821443L;

    // Just say a variable is not defined
    public XPkgUndefinedVarException(String varName) {
        super("Undefined variable: '" + varName + "' is not defined");
    }

    private XPkgUndefinedVarException(int line, XPkgRuntimeException e) {
        super("Error at line " + line + ": " + e.getMessage(), e);
    }

    // Add the line to the message
    @Override
    public XPkgUndefinedVarException setLine(int line) {
        return new XPkgUndefinedVarException(line, this);
    }
}
