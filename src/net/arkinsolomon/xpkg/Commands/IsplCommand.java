package net.arkinsolomon.xpkg.Commands;

import java.util.Arrays;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.QuickHandles;
import net.arkinsolomon.xpkg.Exceptions.XPkgArgLenException;
import net.arkinsolomon.xpkg.Exceptions.XPkgException;
import net.arkinsolomon.xpkg.Exceptions.XPkgImmutableVarException;
import net.arkinsolomon.xpkg.Exceptions.XPkgInternalException;
import net.arkinsolomon.xpkg.Exceptions.XPkgInvalidCallException;
import net.arkinsolomon.xpkg.Exceptions.XPkgInvalidVarNameException;
import net.arkinsolomon.xpkg.Exceptions.XPkgUndefinedVarException;
import net.arkinsolomon.xpkg.Vars.XPkgBool;

//This command determines if a path or variable is pathlike, and stores the boolean result in a variable
public class IsplCommand extends Command {
	public static void execute(String[] args, ExecutionContext context)
			throws XPkgArgLenException, XPkgInvalidVarNameException, XPkgUndefinedVarException,
			XPkgInvalidCallException, XPkgImmutableVarException, XPkgException {

		// Argument checking
		if (args.length < 2)
			throw new XPkgArgLenException(CommandName.ISPL, 2, args.length);

		// Variable to assign the result to
		String assignee = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);

		// Make sure the assignee is valid
		if (!ParseHelper.isValidVarName(assignee))
			throw new XPkgInvalidVarNameException(assignee);

		// The string that might be a path
		String testStr;
		try {
			testStr = ParseHelper.getStr(args, context);
		} catch (XPkgInternalException e) {
			throw QuickHandles.handleGetStr(CommandName.ISPL, e);
		}

		// Check the path name and set he value
		boolean isValid = ParseHelper.isValidPath(testStr);
		context.setVar(assignee, new XPkgBool(isValid));
	}
}
