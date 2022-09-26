package net.arkinsolomon.xpkg.Commands;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.Exceptions.XPkgArgLenException;

// This simple command prints the current execution context
public class ContextCommand extends Command {

	public static void execute(String[] args, ExecutionContext context) throws XPkgArgLenException {
		if (args.length > 0)
			throw new XPkgArgLenException(CommandName.CONTEXT, args.length);
		context.printContext();
	}
}
