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
import net.xpkgclient.exceptions.XPkgNotDirectoryException;
import net.xpkgclient.exceptions.XPkgNotPathLikeException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgFile;
import net.xpkgclient.vars.XPkgResource;
import net.xpkgclient.vars.XPkgVar;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;

class ResolveCommand extends Command {

    /**
     * The class execution command.
     *
     * @param args    The arguments to the command. There should not be any.
     * @param context The execution context to print.
     * @throws XPkgArgLenException Thrown if there are not enough arguments provided.
     */
    public static void execute(String @NotNull [] args, ExecutionContext context) throws XPkgException {
        if (args.length < 3)
            throw new XPkgArgLenException(CommandName.RESOLVE, 3, args.length);

        String assigneeVarName = args[0];
        String fileRootVarName = args[1];
        args = Arrays.copyOfRange(args, 2, args.length);
        String addingPath;
        try {
            addingPath = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.RESOLVE, e);
        }

        if (!ParseHelper.isValidVarName(assigneeVarName))
            throw new XPkgInvalidVarNameException(CommandName.RESOLVE, assigneeVarName);
        if (!ParseHelper.isValidVarName(fileRootVarName))
            throw new XPkgInvalidVarNameException(CommandName.RESOLVE, fileRootVarName);
        if (!context.hasVar(fileRootVarName))
            throw new XPkgUndefinedVarException(fileRootVarName);
        XPkgVar unknownTFileRoot = context.getVar(fileRootVarName);

        String rootPath;
        if (unknownTFileRoot instanceof XPkgResource fileRoot) {
            rootPath = fileRoot.toString();
        } else if (unknownTFileRoot instanceof XPkgFile fileRoot) {
            if (!fileRoot.isDirectory())
                throw new XPkgNotDirectoryException(fileRoot.getValue());
            rootPath = fileRoot.toString();
        } else
            throw new XPkgTypeMismatchException(CommandName.RESOLVE, "second", new VarType[]{VarType.RESOURCE, VarType.MUTABLERESOURCE, VarType.FILE}, unknownTFileRoot.getVarType());

        if (!ParseHelper.isValidPath(addingPath))
            throw new XPkgNotPathLikeException(CommandName.RESOLVE, addingPath);

        File pointFile = new File(rootPath, addingPath);
        XPkgFile pointFileVar = new XPkgFile(pointFile);
        context.setVar(assigneeVarName, pointFileVar);
    }
}
