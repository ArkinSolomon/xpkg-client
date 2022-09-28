package net.xpkgclient.exceptions;

import net.xpkgclient.commands.CommandName;
import net.xpkgclient.vars.VarType;

// This class provides static functions for quick exception handling of internal exceptions
public class QuickHandles {

    // Methods to handle ParseHelper.getStr();
    public static XPkgException handleGetStr(CommandName cmd, XPkgInternalException e) {
        if (e.getMessage().equalsIgnoreCase("arglen"))
            return new XPkgArgLenException(cmd, 2, "a variable is the second argument");
        else if (e.getMessage().equalsIgnoreCase("mismatch"))
            return new XPkgTypeMismatchException(cmd, "second", VarType.STRING, (VarType) e.getData(), e);
        else
            return new XPkgException("An unknown exception occurred", e);
    }
}
