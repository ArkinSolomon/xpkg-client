package net.arkinsolomon.xpkg.Commands;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;
import net.arkinsolomon.xpkg.Vars.VarType;
import net.arkinsolomon.xpkg.Vars.XPkgString;
import net.arkinsolomon.xpkg.Vars.XPkgVar;

//This command joins two paths
public class JoinpCommand extends Command {
	public static void execute(String args[], ExecutionContext context)
			throws InvalidScriptException, ProgrammerError, ScriptExecutionException {
		
		// Check arguments
		if (args.length < 2)
			throw new InvalidScriptException("The 'joinp' command requires 2 arguments at a minimum");

		// Check if the variable is valid
		if (!ParseHelper.isValidVarName(args[0]))
			throw new InvalidScriptException("The first argument of a 'joinp' command must be a variable");
		String assignee = args[0];
		if (!context.hasVar(args[0]))
			context.setVar(assignee, new XPkgString(""));
		else {
			
			//Make sure the variable is a string and valid
			XPkgVar assigneeVar = context.getVar(assignee);
			if (assigneeVar.getVarType() != VarType.STRING)
				throw new InvalidScriptException(
						"Type mismatch: The first argument of a 'joinp' needs to be a variable of type 'STRING'");
			if (!ParseHelper.isValidPath(((XPkgString) assigneeVar).getValue())) 
				throw new InvalidScriptException(
						"Type mismatch: The first argument of a 'joinp' is required to be a variable that is pathlike string");
		}
		
		//Get the second part of the path and combine them
		String secondHalf = ParseHelper.getStr(args, context);
		
		XPkgString assigneeVar = (XPkgString) context.getVar(assignee);
		Path newPath = Paths.get(assigneeVar.getValue(), secondHalf);
		assigneeVar.setValue(newPath.toString());
	}
}
