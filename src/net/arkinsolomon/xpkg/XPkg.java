package net.arkinsolomon.xpkg;

import net.arkinsolomon.xpkg.Commands.*;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;

public class XPkg {

	public static void main(String[] args) {

		// Register all commands
		try {
			Command.registerCmd(CommandName.GET, GetCommand.class);
			Command.registerCmd(CommandName.PRINT, PrintCommand.class);
		} catch (ProgrammerError e) {
			System.out.println(e);
		}

		// Execute a test script
		ScriptExecutionHandler.executeFile("/Users/arkinsolomon/Desktop/test.xpkg");
	}
}
