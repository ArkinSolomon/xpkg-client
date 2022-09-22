package net.arkinsolomon.xpkg.Exceptions;

// A simple exception when an item has not been set
public class XPkgNotYetSetException extends XPkgInvalidCallException {

	// Serial identifier
	private static final long serialVersionUID = 8964432147160549873L;

	public XPkgNotYetSetException(String var) {
		super("'" + var + "' has not yet been set");
	}
}
