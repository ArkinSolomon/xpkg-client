package net.arkinsolomon.xpkg.Exceptions;

// Top level exception for errors that occur while running
public class XPkgRuntimeException extends XPkgScriptRunnerException implements ILineException<XPkgRuntimeException> {

	// Serial identifier
	private static final long serialVersionUID = -8557642804365998541L;

	// Class constructors (same as superclass's)
	public XPkgRuntimeException() {
		super();
	}

	public XPkgRuntimeException(String message) {
		super(message);
	}

	public XPkgRuntimeException(String message, Exception cause) {
		super(message, cause);
	}

	public XPkgRuntimeException(Exception cause) {
		super(cause);
	}

	private XPkgRuntimeException(int line, XPkgRuntimeException e) {
		this("Error at line " + line + ": " + e.getMessage(), e);
	}

	// Add the line to the message
	@Override
	public XPkgRuntimeException setLine(int line) {
		return new XPkgRuntimeException(line, this);
	}

}
