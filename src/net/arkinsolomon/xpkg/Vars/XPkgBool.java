package net.arkinsolomon.xpkg.Vars;

//This variable type is a boolean, true or false
public class XPkgBool extends XPkgVar {
	
	//The data that this is storing
	private boolean data;

	//Create a new boolean
	public XPkgBool(boolean value) {
		data = value;
	}
	
	@Override
	public VarType getVarType() {
		return VarType.BOOL;
	}

	@Override
	public String toString() {
		return data ? "TRUE" : "FALSE";
	}
	
	@Override
	public XPkgBool copy() {
		return new XPkgBool(getValue());
	}
	
	//Get or set the value
	public void setValue(boolean newValue) {
		data = newValue;
	}
	public boolean getValue() {
		return data;
	}
}
