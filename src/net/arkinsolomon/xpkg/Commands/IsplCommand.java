package net.arkinsolomon.xpkg.Commands;

import java.util.Arrays;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;
import net.arkinsolomon.xpkg.Vars.XPkgBool;

//This command determines if a path or variable is pathlike, and stores the boolean result in a variable
public class IsplCommand extends Command {
	public static void execute(String[] args, ExecutionContext context)
			throws InvalidScriptException, ProgrammerError, ScriptExecutionException {

		// Argument checking
		if (args.length < 2)
			throw new InvalidScriptException(
					"Ispl command requires two arguments at minimum, only " + args.length + "provided");

		// Variable to assign the result to
		String assignee = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);

		// Make sure the assignee is valid
		if (!ParseHelper.isValidVarName(assignee))
			throw new InvalidScriptException("Ispl command attempted to assign to invalid variable name");

		// The string that might be a path
		String testStr = ParseHelper.getStr(args, context);

		// Check the path name and set he value
		boolean isValid = ParseHelper.isValidPath(testStr);
		context.setVar(assignee, new XPkgBool(isValid));

	}
}
