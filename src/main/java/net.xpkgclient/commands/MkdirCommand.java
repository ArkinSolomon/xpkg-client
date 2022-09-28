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

import net.xpkgclient.Configuration;
import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ParseHelper;
import net.xpkgclient.exceptions.QuickHandles;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgNotPathLikeException;

import java.io.File;

//This class creates a single directory (not the parent directories) within the X-Plane directory
public class MkdirCommand extends Command {

    @SuppressWarnings("ResultOfMethodCallIgnored")
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
