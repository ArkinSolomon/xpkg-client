package net.xpkgclient.vars;

public class XPkgString extends XPkgVar {

    // The data that this variable stores
    private String data;

    // Create a new string
    public XPkgString(String value) {
        data = value;
    }

    @Override
    public VarType getVarType() {
        return VarType.STRING;
    }

    @Override
    public String toString() {
        return data;
    }

    @Override
    public XPkgString copy() {
        return new XPkgString(getValue());
    }

    public String getValue() {
        return data;
    }

    // Get or set the string
    public void setValue(String newValue) {
        data = newValue;
    }
}