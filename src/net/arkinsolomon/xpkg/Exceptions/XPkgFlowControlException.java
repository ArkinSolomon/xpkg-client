package net.arkinsolomon.xpkg.Exceptions;

// Exceptions thrown with flow control errors
public class XPkgFlowControlException extends XPkgParseException {

	// Serial identifier
	private static final long serialVersionUID = 2081401572143123366L;

	public XPkgFlowControlException(String message) {
		super("Flow control error: " + message);
	}
}
