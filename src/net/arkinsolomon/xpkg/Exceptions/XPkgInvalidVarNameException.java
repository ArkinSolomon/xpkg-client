package net.arkinsolomon.xpkg.Exceptions;

// Exception thrown when a variable name is invalid
public class XPkgInvalidVarNameException extends XPkgParseException {

	// Serial identifier
	private static final long serialVersionUID = -2027953150186197578L;

	// Basic constructor
	public XPkgInvalidVarNameException(String name) {
		super("Invalid variable name: '" + name + "' is not a valid variable");
	}
}
