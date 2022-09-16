package net.arkinsolomon.xpkg.Vars;

import java.io.File;

//This class represents a resource 
public class XPkgResource extends XPkgVar {

	// The resource file
	private File resourcePath = null;

	// Create a new resource variable pointing to the file
	public XPkgResource(File path) {
		resourcePath = path;
	}

	@Override
	public VarType getVarType() {
		return VarType.RESOURCE;
	}

	@Override
	public String toString() {
		return resourcePath.toString();
	}

	@Override
	public XPkgResource copy() {
		return new XPkgResource(resourcePath);
	}

	// Getter and setter for the path
	public void setValue(File path) {
		resourcePath = path;
	}

	public File getValue() {
		return resourcePath;
	}
}
