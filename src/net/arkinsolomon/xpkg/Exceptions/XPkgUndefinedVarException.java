package net.arkinsolomon.xpkg.Exceptions;

//Exception thrown when a variable is undefined
public class XPkgUndefinedVarException extends XPkgRuntimeException {

	// Serial identifier
	private static final long serialVersionUID = -2122679038771821443L;

	// Just say a variable is not defined
	public XPkgUndefinedVarException(String varName) {
		super("Undefined variable: '" + varName + "' is not defined");
	}
}
