package net.arkinsolomon.xpkg.Exceptions;

//This class is used for errors that we know the programmer made, usually put in places that we know should be unreachable
public class ProgrammerError extends Exception {
	
	//Serial identifier
	private static final long serialVersionUID = -2366260590292052596L;

	public ProgrammerError(String message) {
		super(message);
	}
}
