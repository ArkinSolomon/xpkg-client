package net.arkinsolomon.xpkg.Commands;

import net.arkinsolomon.xpkg.ExecutionContext;

// This simple command prints the current execution context
public class ContextCommand extends Command {

	public static void execute(String[] args, ExecutionContext context) {
		context.printContext();
	}
}
