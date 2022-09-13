package net.arkinsolomon.xpkg.Commands;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;

//This class simply prints something
public class PrintCommand extends Command {

	public static void execute(String[] args, ExecutionContext context) throws ScriptExecutionException {

		// If the first argument is a variable and that's all there is print it,
		// otherwise print all arguments
		if (ParseHelper.isValidVarName(args[0])) {
			if (args.length != 1) {
				throw new ScriptExecutionException(
						"Invalid amount of arguments to print statment, first argument is variable, but contains more than one item in argument list");
			}
			System.out.println(context.getVar(args[0]));
			return;
		}
		System.out.println(String.join(" ", args));
	}
}
