package net.arkinsolomon.xpkg.Commands;

import net.arkinsolomon.xpkg.Configuration;
import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.XPkgArgLenException;
import net.arkinsolomon.xpkg.Exceptions.XPkgInvalidCallException;
import net.arkinsolomon.xpkg.Exceptions.XPkgUndefinedVarException;

//This class simply prints something
public class PrintCommand extends Command {

	public static void execute(String[] args, ExecutionContext context)
			throws XPkgArgLenException, XPkgInvalidCallException, XPkgUndefinedVarException {

		// Make sure there is at least one argument
		if (args.length == 0)
			throw new XPkgArgLenException(CommandName.PRINT, 1, 0);

		// If the first argument is a variable and that's all there is print it,
		// otherwise print all arguments
		if (ParseHelper.isValidVarName(args[0])) {

			// Do some more checks
			if (args.length != 1)
				throw new XPkgArgLenException(CommandName.PRINT, 2, "a variable is the first argument");

			if (!context.hasVar(args[0]))
				throw new XPkgUndefinedVarException(args[0]);

			String varValue = context.getVar(args[0]).toString();
			if (Configuration.getInlinePrint())
				System.out.print(varValue);
			else
				System.out.println(varValue);
			return;
		}
		String printStr = String.join(" ", args);
		if (Configuration.getInlinePrint())
			System.out.print(printStr);
		else
			System.out.println(printStr);
	}
}