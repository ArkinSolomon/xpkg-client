package net.arkinsolomon.xpkg.Commands;

import java.util.Arrays;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Vars.XPkgString;

//This command just sets the value of a variable to a string
public class SetstrCommand extends Command {
	public static void execute(String[] args, ExecutionContext context) throws InvalidScriptException {

		// Make sure there are arguments
		if (args.length <= 1)
			throw new InvalidScriptException("Setstr command requires 2 arguments, " + args.length + " provided");

		// Get the assigned variable
		String assignee = args[0];
		if (!ParseHelper.isValidVarName(assignee))
			throw new InvalidScriptException("Variable name '" + assignee + "' is invalid");
		args = Arrays.copyOfRange(args, 1, args.length);

		// Make a new string
		XPkgString newStr = new XPkgString(String.join(" ", args));
		context.setVar(assignee, newStr);
	}
}