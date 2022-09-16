package net.arkinsolomon.xpkg.Commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.InvalidScriptException;
import net.arkinsolomon.xpkg.Exceptions.ProgrammerError;

//This class creates a directory as well as parent directories within the X-Plane directory
public class MkdirsCommand extends Command {

	public static void execute(String[] args, ExecutionContext context)
			throws InvalidScriptException, ProgrammerError, IOException {

		// Argument checking
		if (args.length < 1)
			throw new InvalidScriptException("Mkdirs command requires one argument minimum");

		// The path of the new directory and parent directory to create
		String pathToCreate = ParseHelper.getStr(args, context);

		// Check the path name
		if (!ParseHelper.isValidPath(pathToCreate))
			throw new InvalidScriptException(
					"Tried to make directory and parent directories with an invalid path (" + pathToCreate + ")");

		// Remove the leading slash and make the directories
		pathToCreate = pathToCreate.substring(1);
		Files.createDirectories(Path.of(new File(context.getXpDir(), pathToCreate).toString()));
	}
}
