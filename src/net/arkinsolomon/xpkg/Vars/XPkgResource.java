package net.arkinsolomon.xpkg.Vars;

import java.nio.file.Path;

//This class represents a resource 
public class XPkgResource extends XPkgVar {

	// Path of the resource
	private Path resourcePath = null;

	// Create a new resource variable pointing to the path
	public XPkgResource(Path path) {
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
	public void setValue(Path path) {
		resourcePath = path;
	}

	public Path getValue() {
		return resourcePath;
	}
}
