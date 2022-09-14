package net.arkinsolomon.xpkg;

import net.arkinsolomon.xpkg.Commands.Command;
import net.arkinsolomon.xpkg.Commands.CommandName;
import net.arkinsolomon.xpkg.Commands.GetCommand;
import net.arkinsolomon.xpkg.Commands.PrintCommand;
import net.arkinsolomon.xpkg.Commands.SetCommand;
import net.arkinsolomon.xpkg.Commands.SetstrCommand;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;

public class XPkg {

	public static void main(String[] args) {

		// Register all commands
		try {
			Command.registerCmd(CommandName.GET, GetCommand.class);
			Command.registerCmd(CommandName.PRINT, PrintCommand.class);
			Command.registerCmd(CommandName.SET, SetCommand.class);
			Command.registerCmd(CommandName.SETSTR, SetstrCommand.class);
		} catch (ProgrammerError e) {
			System.out.println(e);
		}
	

		// Execute a test script
		ScriptExecutionHandler.executeFile("/Users/arkinsolomon/Desktop/test.xpkgs");
	}
}
