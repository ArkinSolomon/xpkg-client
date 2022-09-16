package net.arkinsolomon.xpkg.Commands;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;

//This class simply prints something
public class PrintCommand extends Command {

	public static void execute(String[] args, ExecutionContext context) throws InvalidScriptException, ProgrammerError {

		// Make sure there is at least one argument
		if (args.length == 0)
			throw new InvalidScriptException("Print command requires more than one argument");

		// If the first argument is a variable and that's all there is print it,
		// otherwise print all arguments
		if (ParseHelper.isValidVarName(args[0])) {

			// Do some more checks
			if (args.length != 1)
				throw new InvalidScriptException(
						"Invalid amount of arguments to print command, first argument is a variable, but contains more than one item in argument list");

			if (!context.hasVar(args[0]))
				throw new InvalidScriptException("Error: variable '" + args[0] + "' does not exist");

			System.out.println(context.getVar(args[0]));
			return;
		}
		System.out.println(String.join(" ", args));
	}
}
