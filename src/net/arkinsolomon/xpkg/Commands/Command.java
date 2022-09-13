package net.arkinsolomon.xpkg.Commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;
import net.arkinsolomon.xpkg.Exceptions.ScriptExecutionException;

//Empty class to allow subclasses to inherit from Command
public abstract class Command {

	// Store all commands
	private static HashMap<CommandName, Method> cmds = new HashMap<CommandName, Method>();

	// Register a command
	public static void registerCmd(CommandName cName, Class<? extends Command> c) throws ProgrammerError {

		// Make sure command has execute() method
		Method m;
		try {
			m = c.getMethod("execute", String[].class, ExecutionContext.class);
		} catch (SecurityException e) {
			throw e;
		} catch (NoSuchMethodException e) {
			throw new ProgrammerError(
					"Attempted to register command that inherits from the command class but does not have an execute() method");
		}

		// Make sure this isn't a duplicate
		if (cmds.containsKey(cName))
			throw new ProgrammerError("Attempted to register command twice");

		cmds.put(cName, m);
	}

	// Execute a command
	public static void call(CommandName n, String[] args, ExecutionContext context)
			throws ScriptExecutionException, ProgrammerError {
		Method m = cmds.get(n);
		if (m == null) {
			throw new ProgrammerError(
					"As a user, you shouldn't see this error... " + "there was a command that hasn't been registered");
		}
		try {
			m.invoke("execute", args, context);
		} catch (IllegalAccessException e) {
			throw new ScriptExecutionException("Could not execute command'" + n + "': illegal access");
		} catch (IllegalArgumentException e) {
			throw new ScriptExecutionException("Could not execute command'" + n + "': illegal arguments");
		} catch (InvocationTargetException e) {
			throw new ScriptExecutionException("Could not execute command'" + n + "': invalid invocation");
		}
	}
}
