package net.xpkgclient.commands;

import net.xpkgclient.ExecutionContext;

//This class gets and stores a file
public class GetCommand extends Command {

    public static void execute(String[] args, ExecutionContext context) {
        System.out.println("GET COMMAND EXECUTION: " + args[0] + ", " + args[1]);
//
//		// Make sure args[0] is a valid variable
//		if (!ParseHelper.isValidVarName(args[0]))
//			throw new XPkgTypeMismatchException(CommandName.GET, 'first', , g);
//
//		// Create the new variable or get it
//		XPkgString newVar = new XPkgString(args[1] + "_AS_RESOURCE");
//		context.setVar(args[0], newVar);
    }
}
