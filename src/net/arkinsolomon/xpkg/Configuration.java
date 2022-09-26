package net.arkinsolomon.xpkg;

import java.io.File;

//All configuration and settings for the client
public class Configuration {

	// Get or set the currently active X-Plane path
	private static File xpPath = null;

	public static File getXpPath() {
		return xpPath;
	}

	public static void setXpPath(File xpPath) {
		Configuration.xpPath = xpPath;
	}

	// True if the PRINT command should print using print() instead of println(),
	// used for testing
	private static boolean inlinePrint = false;

	public static boolean getInlinePrint() {
		return inlinePrint;
	}

	public static void setInlinePrint(boolean inlinePrint) {
		Configuration.inlinePrint = inlinePrint;
	}
}
