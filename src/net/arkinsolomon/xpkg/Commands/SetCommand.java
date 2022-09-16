package net.arkinsolomon.xpkg.Commands;

import java.util.Arrays;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;
import net.arkinsolomon.xpkg.Vars.XPkgBool;
import net.arkinsolomon.xpkg.Vars.XPkgVar;

//This class sets a variable based on multiple different things
public class SetCommand extends Command {

	public static void execute(String[] args, ExecutionContext context)
			throws ScriptExecutionException, InvalidScriptException, ProgrammerError {

		// Make sure there are enough arguments
		if (args.length <= 1)
			throw new InvalidScriptException("Set command requires 2 arguments, " + args.length + " provided");

		// Get the assigned variable
		String assignee = args[0];
		if (!ParseHelper.isValidVarName(assignee))
			throw new InvalidScriptException("Variable name '" + assignee + "' is invalid");
		args = Arrays.copyOfRange(args, 1, args.length);

		// If it's a variable just copy it
		if (args.length == 1 && ParseHelper.isValidVarName(args[0])) {

			// Make sure the variable exists
			if (!context.hasVar(args[0])) {
				throw new ScriptExecutionException("Error assigning variable: '" + args[0] + "' does not exist");
			}

			XPkgVar originalVar = context.getVar(args[0]);
			XPkgVar newVar = originalVar.copy();
			context.setVar(assignee, newVar);
			return;
		}

		// Set the variable to true if it evaluates to trueÏ
		context.setVar(assignee, new XPkgBool(ParseHelper.isTrue(args, context)));
	}
}