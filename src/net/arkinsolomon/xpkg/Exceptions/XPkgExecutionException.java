package net.arkinsolomon.xpkg.Exceptions;

// Top level exception for exceptions that are not the script programmers fault, but rather an error with the OS or XPkg
public class XPkgExecutionException extends XPkgScriptRunnerException {

	// Serial identifier
	private static final long serialVersionUID = 3113973855303520458L;

	// Class constructors (same as superclass's)
	public XPkgExecutionException() {
		super();
	}

	public XPkgExecutionException(String message) {
		super(message);
	}

	public XPkgExecutionException(String message, Exception cause) {
		super(message, cause);
	}

	public XPkgExecutionException(Exception cause) {
		super(cause);
	}
}
