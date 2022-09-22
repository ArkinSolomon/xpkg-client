package net.arkinsolomon.xpkg.Exceptions;

// Exception thrown when a boolean statement is invalid
public class XPkgInvalidBoolStatement extends XPkgParseException {

	// Serial identifier
	private static final long serialVersionUID = 3542483398964626556L;

	//When there is an invalid item
	public XPkgInvalidBoolStatement(String item) {
		super("Invalid item in boolean statment: '" + item + "'");
	}
}
