package net.arkinsolomon.xpkg.Commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.Exceptions.XPkgExecutionException;
import net.arkinsolomon.xpkg.Exceptions.XPkgUnimplementedException;


// Class that allows commands to be called and managed
public abstract class Command {

	// Store all commands
	private static HashMap<CommandName, Method> cmds = new HashMap<CommandName, Method>();

	// True if we've registered commands already
	private static boolean hasRegisteredCommands = false;

	// Register all commands
	public static void registerCommands() throws XPkgExecutionException {
		
		//Only register once
		if (hasRegisteredCommands)
			return;
		hasRegisteredCommands = true;
		
		//Register all commands
		registerCmd(CommandName.GET, GetCommand.class);
		registerCmd(CommandName.PRINT, PrintCommand.class);
		registerCmd(CommandName.SET, SetCommand.class);
		registerCmd(CommandName.SETSTR, SetstrCommand.class);
		registerCmd(CommandName.MKDIR, MkdirCommand.class);
		registerCmd(CommandName.MKDIRS, MkdirsCommand.class);
		registerCmd(CommandName.ISPL, IsplCommand.class);
		registerCmd(CommandName.JOIN, JoinCommand.class);
		registerCmd(CommandName.JOINP, JoinpCommand.class);
		registerCmd(CommandName.CONTEXT, ContextCommand.class);
	}

	// Register a command
	private static void registerCmd(CommandName cName, Class<? extends Command> c) throws XPkgExecutionException {

		// Make sure command has execute() method
		Method m;
		try {
			m = c.getMethod("execute", String[].class, ExecutionContext.class);
		} catch (SecurityException e) {
			throw new XPkgExecutionException(e);
		} catch (NoSuchMethodException e) {
			throw new XPkgUnimplementedException("'" + c.getName() + "' does not have a valid execute() method");
		}

		// Make sure this isn't a duplicate
		if (cmds.containsKey(cName))
			throw new XPkgExecutionException("Attempted to register command '" + cName + "' more than once");

		cmds.put(cName, m);
	}

	// Execute a command
	public static void call(CommandName n, String[] args, ExecutionContext context) throws Throwable {
		Method m = cmds.get(n);
		if (m == null)
			throw new XPkgUnimplementedException("The command '" + n
					+ "' hasn't been registered or is an improperly handled flow control statement");

		// Pass the exceptions up to main
		try {
			m.invoke("execute", args, context);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw e.getCause();
		}
	}
}
