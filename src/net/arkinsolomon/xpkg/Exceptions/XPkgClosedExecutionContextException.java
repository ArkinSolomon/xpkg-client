package net.arkinsolomon.xpkg.Exceptions;

//Exception thrown when the execution context is closed
public class XPkgClosedExecutionContextException extends XPkgInvalidCallException {
	
	//Serial identifier
	private static final long serialVersionUID = 813342998451479620L;
	
	//Use a default message
	public XPkgClosedExecutionContextException() {
		super("Execution context has been closed and is not active");
	}
}
