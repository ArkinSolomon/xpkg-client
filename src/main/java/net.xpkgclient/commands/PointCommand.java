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
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.exceptions.XPkgNoFileException;
import net.xpkgclient.exceptions.XPkgNotDirectoryException;
import net.xpkgclient.exceptions.XPkgNotPathLikeException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgMutableResource;
import net.xpkgclient.vars.XPkgResource;
import net.xpkgclient.vars.XPkgVar;

import java.io.File;
import java.util.Arrays;

/**
 * This command creates a new mutable resource or resource which points to a directory inside a mutable resource or resource.
 */
class PointCommand extends Command {

    /**
     * The class execution command.
     *
     * @param args    The arguments to the command. There should not be any.
     * @param context The execution context to print.
     * @throws XPkgException Thrown if there is a type mismatch, invalid format, or other error.
     */
    public static void execute(String[] args, ExecutionContext context) throws XPkgException {
        if (args.length < 3)
            throw new XPkgArgLenException(CommandName.POINT, 3, args.length);

        String assigneeVarName = args[0];
        String resourceRootVarName = args[1];
        args = Arrays.copyOfRange(args, 2, args.length);
        String addingPath;
        try {
            addingPath = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.POINT, e);
        }

        if (!ParseHelper.isValidVarName(assigneeVarName))
            throw new XPkgInvalidVarNameException(CommandName.POINT, assigneeVarName);
        if (!ParseHelper.isValidVarName(resourceRootVarName))
            throw new XPkgInvalidVarNameException(CommandName.POINT, resourceRootVarName);
        if (!context.hasVar(resourceRootVarName))
            throw new XPkgUndefinedVarException(resourceRootVarName);
        XPkgVar unknownTResourceRoot = context.getVar(resourceRootVarName);

        if (!(unknownTResourceRoot instanceof XPkgResource resourceRoot))
            throw new XPkgTypeMismatchException(CommandName.POINT, "second", new VarType[]{VarType.RESOURCE, VarType.MUTABLERESOURCE}, unknownTResourceRoot.getVarType());

        if (!ParseHelper.isValidPath(addingPath))
            throw new XPkgNotPathLikeException(CommandName.POINT, addingPath);

        File newResourceFile = new File(resourceRoot.getValue(), addingPath);
        if (!newResourceFile.exists())
            throw new XPkgNoFileException(newResourceFile);
        if (newResourceFile.isFile())
            throw new XPkgNotDirectoryException(newResourceFile);

        XPkgVar newResourceVar = new XPkgResource(newResourceFile);
        if (unknownTResourceRoot.getVarType() == VarType.MUTABLERESOURCE)
            newResourceVar = new XPkgMutableResource(newResourceFile);
        context.setVar(assigneeVarName, newResourceVar);
    }
}
