package net.arkinsolomon.xpkg.Exceptions;

import net.arkinsolomon.xpkg.Commands.CommandName;
import net.arkinsolomon.xpkg.Vars.VarType;

// This class provides static functions for quick exception handling of internal exceptions
public final class QuickHandles {

	// Methods to handle ParseHelper.getStr();
	public static XPkgException handleGetStr(CommandName cmd, XPkgInternalException e) {
		if (e.getMessage().equalsIgnoreCase("args"))
			return new XPkgArgLenException(cmd, 2, "a variable is the second argument");
		else
			return new XPkgTypeMismatchException(cmd, "second", VarType.STRING, (VarType) e.getData(), e);
	}
}
