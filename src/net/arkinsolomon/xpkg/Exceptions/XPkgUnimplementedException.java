package net.arkinsolomon.xpkg.Exceptions;

// An exception thrown when a class or method is not registered or implemented
public class XPkgUnimplementedException extends XPkgExecutionException {

	//Serial identifier
	private static final long serialVersionUID = -3064139511429346241L;

	// Provide a message
	public XPkgUnimplementedException(String message) {
		super("Unimplemented exception [not a script issue, but a problem with X-Pkg]:" + message);
	}
}
