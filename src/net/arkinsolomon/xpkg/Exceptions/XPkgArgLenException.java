package net.arkinsolomon.xpkg.Exceptions;

import net.arkinsolomon.xpkg.Commands.CommandName;

// Used when argument lengths do not match up
public class XPkgArgLenException extends XPkgParseException {

	// Serial identifier
	private static final long serialVersionUID = 4663186103057388801L;

	// Basic argument length mismatch
	public XPkgArgLenException(CommandName cmd, int expected, int actual) {
		super("Invalid argument length: The " + cmd + " command expects " + expected + " arguments, got " + actual);
	}
	
	// Too many arguments with a note for a reason
	public XPkgArgLenException(CommandName cmd, int max, String reason) {
		super("Invalid argument length: The " + cmd + " command expects " + max + " arguments if  " + reason);
	} 
	
	// Command takes no arguments
	public XPkgArgLenException(CommandName cmd, int given) {
		super("Invalid argument length: The " + cmd + " command takes no arguments, but " + given + " were given");
	}
}
