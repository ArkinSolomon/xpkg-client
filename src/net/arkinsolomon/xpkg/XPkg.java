package net.arkinsolomon.xpkg;

import net.arkinsolomon.xpkg.Commands.*;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;

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
		
//		try {
//			System.out.println(ParseHelper.isTrue(new String[] {"TRUE", "&", "FALSE"}, new ExecutionContext()));
//		} catch (ScriptExecutionException | InvalidScriptException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	

		// Execute a test script
		ScriptExecutionHandler.executeFile("/Users/arkinsolomon/Desktop/test.xpkg");
	}
}
