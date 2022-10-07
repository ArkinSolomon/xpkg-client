/*
 * Copyright (c) 2022. XPkg-Client Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied limitations under the License.
 */

package net.xpkgclient.commands;

import net.lingala.zip4j.ZipFile;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ParseHelper;
import net.xpkgclient.exceptions.QuickHandles;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgExecutionException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgInvalidResourceIdException;
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.exceptions.XPkgNoResourceException;
import net.xpkgclient.vars.XPkgResource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Get a resource from a server, and store it into a variable.
 */
class GetCommand extends Command {

    private static final File getLoc = new File("/Users/arkinsolomon/Desktop/resources");

    public static void execute(String @NotNull [] args, ExecutionContext context) throws XPkgException {

        if (args.length != 2)
            throw new XPkgArgLenException(CommandName.GET, 2, args.length);

        String assigneeVarName = args[0];
        if (!ParseHelper.isValidVarName(assigneeVarName))
            throw new XPkgInvalidVarNameException(CommandName.GET, assigneeVarName);

        args = Arrays.copyOfRange(args, 1, args.length);
        String resourceId;
        try {
            resourceId = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.GET, e);
        }

        if (!ParseHelper.isValidResourceId(resourceId))
            throw new XPkgInvalidResourceIdException(CommandName.GET, "second", resourceId);

        // For now, get resources from a temporary location on our desktop, so create the file pointer and check for existence after
        String resourceFileName = resourceId + ".xpkgres";
        File resource = new File(getLoc, resourceFileName);
        if (!resource.exists())
            throw new XPkgNoResourceException(resourceId);

        File resourceLoc = new File(context.getResourceStorageLoc(), resourceFileName);
        File zipLoc = new File(context.getResourceDownloadLoc(), resourceFileName);
        try {
            Files.copy(resource.toPath(), zipLoc.toPath());
            @SuppressWarnings("resource") ZipFile f = new ZipFile(zipLoc);
            f.extractAll(context.getResourceStorageLoc().toString());
        } catch (IOException e) {
            throw new XPkgExecutionException(e);
        }

        XPkgResource res = new XPkgResource(resourceLoc);
        context.setVar(assigneeVarName, res);

        //		// Make sure args[0] is a valid variable
        //		if (!ParseHelper.isValidVarName(args[0]))
        //			throw new XPkgTypeMismatchException(CommandName.GET, 'first', , g);
        //
        //		// Create the new variable or get it
        //		XPkgString newVar = new XPkgString(args[1] + "_AS_RESOURCE");
        //		context.setVar(args[0], newVar);
    }
}
