package net.arkinsolomon.xpkg.Exceptions;

// Top level exception for errors that occur when running scripts
public class XPkgScriptRunnerException extends XPkgException {

	// Serial identifier
	private static final long serialVersionUID = -4155621685868796790L;

	// Basic constructors (basically the same as Exception)
	public XPkgScriptRunnerException() {
		this((String) null);
	}

	public XPkgScriptRunnerException(String message) {
		super(message);
	}

	public XPkgScriptRunnerException(String message, Exception cause) {
		super(message, cause);
	}

	public XPkgScriptRunnerException(Exception cause) {
		super(cause);
	}
}
