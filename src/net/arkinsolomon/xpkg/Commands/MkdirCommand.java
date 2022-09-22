package net.arkinsolomon.xpkg.Commands;

import java.io.File;

import net.arkinsolomon.xpkg.Configuration;
import net.arkinsolomon.xpkg.ExecutionContext;
import net.arkinsolomon.xpkg.ParseHelper;
import net.arkinsolomon.xpkg.Exceptions.QuickHandles;
import net.arkinsolomon.xpkg.Exceptions.XPkgArgLenException;
import net.arkinsolomon.xpkg.Exceptions.XPkgException;
import net.arkinsolomon.xpkg.Exceptions.XPkgInternalException;
import net.arkinsolomon.xpkg.Exceptions.XPkgNotPathLikeException;

//This class creates a single directory (not the parent directories) within the X-Plane directory
public class MkdirCommand extends Command {

	public static void execute(String[] args, ExecutionContext context) throws XPkgException {

		// Argument checking
		if (args.length < 1)
			throw new XPkgArgLenException(CommandName.MKDIR, 1, args.length);

		// The path of the new item to create
		String pathToCreate;
		try {
			pathToCreate = ParseHelper.getStr(args, context);
		} catch (XPkgInternalException e) {
			throw QuickHandles.handleGetStr(CommandName.MKDIR, e);
		}

		// Check the path name
		boolean isVar = args[0].startsWith("$");
		if (!ParseHelper.isValidPath(pathToCreate)) {
			if (isVar)
				throw new XPkgNotPathLikeException(CommandName.MKDIR, args[0], pathToCreate);
			else
				throw new XPkgNotPathLikeException(CommandName.MKDIR, pathToCreate);
		}

		// Remove the leading slash and make the directory
		pathToCreate = pathToCreate.substring(1);
		new File(Configuration.getXpPath(), pathToCreate).mkdir();
	}
}
