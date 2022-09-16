package net.arkinsolomon.xpkg.Commands;

import java.io.File;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;

//This class creates a single directory (not the parent directories) within the X-Plane directory
public class MkdirCommand extends Command {

	public static void execute(String[] args, ExecutionContext context) throws InvalidScriptException, ProgrammerError {

		// Argument checking
		if (args.length < 1)
			throw new InvalidScriptException("Mkdirs command requires one argument minimum");

		// The path of the new item to create
		String pathToCreate = ParseHelper.getStr(args, context);

		// Check the path name
		if (!ParseHelper.isValidPath(pathToCreate))
			throw new InvalidScriptException("Tried to make directory with an invalid path (" + pathToCreate + ")");

		// Remove the leading slash and make the directory
		pathToCreate = pathToCreate.substring(1);
		new File(context.getXpDir(), pathToCreate).mkdir();
	}
}
