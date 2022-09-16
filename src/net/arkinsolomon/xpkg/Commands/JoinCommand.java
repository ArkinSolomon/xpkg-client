package net.arkinsolomon.xpkg.Commands;

import java.util.Arrays;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;
import net.arkinsolomon.xpkg.Vars.VarType;
import net.arkinsolomon.xpkg.Vars.XPkgString;
import net.arkinsolomon.xpkg.Vars.XPkgVar;

//This class concats two strings together and stores it in the first variable
public class JoinCommand extends Command {

	public static void execute(String args[], ExecutionContext context)
			throws InvalidScriptException, ProgrammerError, ScriptExecutionException {

		// Check arguments
		if (args.length < 2)
			throw new InvalidScriptException("The 'join' command requires 2 arguments at a minimum");

		// Check if the variable is valid
		if (!ParseHelper.isValidVarName(args[0]))
			throw new InvalidScriptException("The first argument of a 'join' command must be a variable");
		String assignee = args[0];
		if (!context.hasVar(args[0]))
			context.setVar(assignee, new XPkgString(""));
		else {
			XPkgVar assigneeVar = context.getVar(assignee);
			if (assigneeVar.getVarType() != VarType.STRING)
				throw new InvalidScriptException(
						"Type mismatch: The first argument of a 'join' needs to be a variable of type 'STRING'");
		}

		// Check if the second argument is a variable
		args = Arrays.copyOfRange(args, 1, args.length);
		
		// The string to add to the variable assignee
		String catString = ParseHelper.getStr(args, context);
		
		//Update the variable in memory
		XPkgString assigneeVar = (XPkgString) context.getVar(assignee);
		String assigneeCurrVal = assigneeVar.getValue();
		assigneeVar.setValue(assigneeCurrVal + catString);
	}
}
