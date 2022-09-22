package net.arkinsolomon.xpkg;

import net.arkinsolomon.xpkg.Commands.Command;
import net.arkinsolomon.xpkg.Commands.CommandName;
import net.arkinsolomon.xpkg.Commands.ContextCommand;
import net.arkinsolomon.xpkg.Commands.GetCommand;
import net.arkinsolomon.xpkg.Commands.IsplCommand;
import net.arkinsolomon.xpkg.Commands.JoinCommand;
import net.arkinsolomon.xpkg.Commands.JoinpCommand;
import net.arkinsolomon.xpkg.Commands.MkdirCommand;
import net.arkinsolomon.xpkg.Commands.MkdirsCommand;
import net.arkinsolomon.xpkg.Commands.PrintCommand;
import net.arkinsolomon.xpkg.Commands.SetCommand;
import net.arkinsolomon.xpkg.Commands.SetstrCommand;

public class XPkg {

	public static void main(String[] args) {

		// Register all commands
		try {
			Command.registerCmd(CommandName.GET, GetCommand.class);
			Command.registerCmd(CommandName.PRINT, PrintCommand.class);
			Command.registerCmd(CommandName.SET, SetCommand.class);
			Command.registerCmd(CommandName.SETSTR, SetstrCommand.class);
			Command.registerCmd(CommandName.MKDIR, MkdirCommand.class);
			Command.registerCmd(CommandName.MKDIRS, MkdirsCommand.class);
			Command.registerCmd(CommandName.ISPL, IsplCommand.class);
			Command.registerCmd(CommandName.JOIN, JoinCommand.class);
			Command.registerCmd(CommandName.JOINP, JoinpCommand.class);
			Command.registerCmd(CommandName.CONTEXT, ContextCommand.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//				throw new XPkgTypeMismatchException("join", "second", VarType.STRING, VarType.RESOURCE);
//		} catch (XPkgRuntimeException e) {
//			throw new XPkgRuntimeException(7, e);
//		}

		// Execute a test script
		ScriptExecutionHandler.executeFile("/Users/arkinsolomon/Desktop/test.xpkgs");
	}
}
