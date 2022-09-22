package net.arkinsolomon.xpkg.Exceptions;

// This exception is called when a method is called that shouldn't be
public class XPkgInvalidCallException extends XPkgExecutionException {

	// Serial identifier
	private static final long serialVersionUID = -7391328873326455824L;

	// Constructor that makes a message
	public XPkgInvalidCallException(String reason) {
		this(reason, 1);
	}
	public XPkgInvalidCallException(String reason, int index) {
		super("Invalid call to method '" + getCallerMethodName(index) + "': " + reason);
	}
	
	//Get the name of the method in the call stack
	private static String getCallerMethodName(int i)
	{
	   return StackWalker.
	      getInstance().
	      walk(stream -> stream.skip(1).findFirst().get()).
	      getMethodName();
	}
}
