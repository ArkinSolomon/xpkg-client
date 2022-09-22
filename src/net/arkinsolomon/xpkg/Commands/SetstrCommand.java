package net.arkinsolomon.xpkg.Commands;

import java.util.Arrays;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.XPkgArgLenException;
import net.arkinsolomon.xpkg.Exceptions.XPkgImmutableVarException;
import net.arkinsolomon.xpkg.Exceptions.XPkgInvalidCallException;
import net.arkinsolomon.xpkg.Exceptions.XPkgInvalidVarNameException;
import net.arkinsolomon.xpkg.Exceptions.XPkgTypeMismatchException;
import net.arkinsolomon.xpkg.Vars.XPkgString;

//This command just sets the value of a variable to a string
public class SetstrCommand extends Command {
	public static void execute(String[] args, ExecutionContext context)
			throws XPkgArgLenException, XPkgInvalidVarNameException, XPkgInvalidCallException,
			XPkgImmutableVarException, XPkgTypeMismatchException {

		// Make sure there are arguments
		if (args.length < 2)
			throw new XPkgArgLenException(CommandName.SETSTR, 2, args.length);

		// Get the assigned variable
		String assignee = args[0];
		if (!ParseHelper.isValidVarName(assignee))
			throw new XPkgInvalidVarNameException(assignee);
		args = Arrays.copyOfRange(args, 1, args.length);

		if (args[0].startsWith("$"))
			throw new XPkgTypeMismatchException(CommandName.SETSTR, "second");

		// Make a new string
		XPkgString newStr = new XPkgString(String.join(" ", args));
		context.setVar(assignee, newStr);
	}
}