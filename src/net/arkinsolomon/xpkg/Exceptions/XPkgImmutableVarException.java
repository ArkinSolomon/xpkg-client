package net.arkinsolomon.xpkg.Exceptions;

// Exception when trying to change an immutable variable
public class XPkgImmutableVarException extends XPkgRuntimeException {

	// Serial identifier
	private static final long serialVersionUID = -5842352310837071330L;

	// Say that the variable is immutable
	public XPkgImmutableVarException(String varName) {
		super("'" + varName + "' is immutable and can not be changed");
	}
}
