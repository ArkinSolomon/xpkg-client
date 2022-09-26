package net.xpkgclient.exceptions;

import net.xpkgclient.commands.CommandName;

import java.io.Serial;

// Exception thrown if variable is not a pathlike when expected
public class XPkgNotPathLikeException extends XPkgRuntimeException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -4254373123050488699L;

    //Say variable was not pathlike
    public XPkgNotPathLikeException(CommandName cmd, String varName, String value) {
        super(cmd + " expected the variable '" + varName + "' to be a pathlike string, got: " + value);
    }

    //Say constant was not path like
    public XPkgNotPathLikeException(CommandName cmd, String value) {
        super(cmd + " expected a pathlike, got: " + value);
    }

}
