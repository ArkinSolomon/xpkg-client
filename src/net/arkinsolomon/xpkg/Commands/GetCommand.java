package net.arkinsolomon.xpkg.Commands;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Vars.XPkgString;

//This class gets and stores a file
public class GetCommand extends Command {

	public static void execute(String[] args, ExecutionContext context)
			throws InvalidScriptException {
		System.out.println("GET COMMAND EXECUTION: " + args[0] + ", " + args[1]);
		
		//Make sure args[0] is a valid variable
		if (!ParseHelper.isValidVarName(args[0]))
			throw new InvalidScriptException("First argument of get must be a variable");
		
		//Create a new variable
		XPkgString newVar = new XPkgString (args[1] + "_AS_RESOURCE");
		context.setVar(args[0], newVar);
	}
}
