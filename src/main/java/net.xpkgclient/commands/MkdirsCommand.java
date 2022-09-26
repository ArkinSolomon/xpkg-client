package net.xpkgclient.commands;

import net.xpkgclient.Configuration;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ParseHelper;
import net.xpkgclient.exceptions.QuickHandles;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgNotPathLikeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//This class creates a directory as well as parent directories within the X-Plane directory
public class MkdirsCommand extends Command {

    public static void execute(String[] args, ExecutionContext context) throws IOException, XPkgException {

        // Argument checking
        if (args.length < 1)
            throw new XPkgArgLenException(CommandName.MKDIRS, 1, args.length);

        // The path of the new directory and parent directory to create
        String pathToCreate;
        try {
            pathToCreate = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.MKDIRS, e);
        }

        // Check the path name
        boolean isVar = args[0].startsWith("$");
        if (!ParseHelper.isValidPath(pathToCreate)) {
            if (isVar)
                throw new XPkgNotPathLikeException(CommandName.MKDIRS, args[0], pathToCreate);
            else
                throw new XPkgNotPathLikeException(CommandName.MKDIRS, pathToCreate);
        }

        // Remove the leading slash and create the directories
        pathToCreate = pathToCreate.substring(1);
        Files.createDirectories(Path.of(new File(Configuration.getXpPath(), pathToCreate).toString()));
    }
}
