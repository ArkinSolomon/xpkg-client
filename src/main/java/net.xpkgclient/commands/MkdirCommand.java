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

import net.xpkgclient.ExecutionContext;
import net.xpkgclient.ParseHelper;
import net.xpkgclient.exceptions.QuickHandles;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgFileExistsException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgNotPathLikeException;
import net.xpkgclient.exceptions.XPkgParentDirException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.filesystem.MkdirOperation;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgMutableResource;
import net.xpkgclient.vars.XPkgVar;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class creates a single directory (not the parent directories) within the X-Plane directory
 */
final class MkdirCommand extends Command {

    /**
     * The command execution method.
     *
     * @param args    The arguments to the command. See the readme for valid arguments.
     * @param context The execution context that this command executes within.
     * @throws XPkgException Can be thrown for multiple reasons such as user error, or a type mismatch, or another reason.
     */
    public static void execute(String @NotNull [] args, ExecutionContext context) throws XPkgException, IOException {
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.MKDIR, 2, args.length);

        // Get the mutable resource
        String resName = args[0];
        XPkgVar unknownTVar;
        try {
            unknownTVar = context.checkExistingVar(resName);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleCheckExistingVar(CommandName.MKDIR, "first", e);
        }
        VarType type = unknownTVar.getVarType();
        if (type != VarType.MUTABLERESOURCE)
            throw new XPkgTypeMismatchException(CommandName.MKDIR, "first", VarType.MUTABLERESOURCE, type);
        XPkgMutableResource resource = (XPkgMutableResource) unknownTVar;

        // The path of the new item to create
        args = Arrays.copyOfRange(args, 1, args.length);
        String pathToCreate;
        try {
            pathToCreate = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.MKDIR, e);
        }

        // We want to throw a different exception if the user provided a variable for better feedback
        boolean isVar = args[0].startsWith("$");
        if (!ParseHelper.isValidPath(pathToCreate)) {
            if (isVar)
                throw new XPkgNotPathLikeException(CommandName.MKDIR, args[0], pathToCreate);
            else
                throw new XPkgNotPathLikeException(CommandName.MKDIR, pathToCreate);
        }

        // The MKDIR operation expects that the file doesn't exist
        File dirToCreate = new File(resource.getValue(), pathToCreate);
        if (dirToCreate.exists())
            throw new XPkgFileExistsException(dirToCreate);
        if (!dirToCreate.getParentFile().exists())
            throw new XPkgParentDirException(dirToCreate);

        context.fileTracker.runOperation(new MkdirOperation(dirToCreate, false));
    }
}
