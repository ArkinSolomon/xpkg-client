package net.xpkgclient.exceptions;

import net.xpkgclient.commands.CommandName;
import net.xpkgclient.vars.VarType;

import java.io.Serial;

//An error that is thrown when type mismatch 
public class XPkgTypeMismatchException extends XPkgRuntimeException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -3787777489114171239L;

    // Throw a new exception with two different types
    public XPkgTypeMismatchException(CommandName command, String argC, VarType expected, VarType actual) {
        this(command, argC, expected, actual, null);
    }

    public XPkgTypeMismatchException(CommandName command, String argC, VarType expected, VarType actual, Exception e) {
        super("The " + command + " command expected the " + argC + " argument to be " + expected + " but got " + actual
                + " instead", e);
    }

    // Throw an exception when the second variable *has* to be a ...STRING
    public XPkgTypeMismatchException(CommandName command, String argC) {
        super("The " + command + " command requires the " + argC + " argument to be a ...STRING");
    }

    // Throw an exception when the variable has to be a certain type without a command
    public XPkgTypeMismatchException(String varName, VarType expected, VarType actual) {
        this(varName, expected, actual, null);
    }

    public XPkgTypeMismatchException(String varName, VarType expected, VarType actual, Exception cause) {
        super("Variable '" + varName + "' was expected to be a " + expected + " but got " + actual + " instead", cause);
    }
}
