package net.arkinsolomon.xpkg;

import java.io.File;

//All configuration and settings for the client
public class Configuration {

	//The X-Plane root directory
//	private static File xpPath;
	
	//Get the X-Plane path
	public static File getXpPath()
	{
		return new File("/Users/arkinsolomon/Desktop/X-Plane 12");
	}
}
