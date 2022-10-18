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

import net.xpkgclient.packagemanager.ExecutionContext;
import net.xpkgclient.packagemanager.ParseHelper;
import net.xpkgclient.exceptions.QuickHandles;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgNotMutableException;
import net.xpkgclient.exceptions.XPkgScriptRuntimeException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.filesystem.RenameOperation;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgFile;
import net.xpkgclient.vars.XPkgMutableResource;
import net.xpkgclient.vars.XPkgVar;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

final class RenameCommand extends Command {

    /**
     * The command execution method.
     *
     * @param args    The arguments to the command. See the readme for valid arguments.
     * @param context The execution context that this command executes within.
     * @throws XPkgException Can be thrown for multiple reasons such as user error, or a type mismatch, or another reason.
     */
    public static void execute(String[] args, ExecutionContext context) throws XPkgException, IOException {
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.RENAME, 2, args.length);

        String assigneeVarName = args[0];
        XPkgVar unknownTFileVar;
        try {
            unknownTFileVar = context.checkExistingVar(assigneeVarName);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleCheckExistingVar(CommandName.RENAME, "first", e);
        }

        if (!(unknownTFileVar instanceof XPkgFile fileVar))
            throw new XPkgTypeMismatchException(CommandName.POINT, "first", VarType.FILE, unknownTFileVar.getVarType());

        if (!fileVar.getParentResource().isMutable())
            throw new XPkgNotMutableException(fileVar.getValue());

        args = Arrays.copyOfRange(args, 1, args.length);
        String newName;
        try {
            newName = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.POINT, e);
        }

        if (ParseHelper.stringContains(newName, "~%/\\") || newName.contains(".."))
            throw new XPkgScriptRuntimeException("Invalid filename: The file '" + fileVar + "' can not be renamed to '" + newName + "' since the new name is invalid");

        RenameOperation op = new RenameOperation(fileVar, newName);
        context.fileTracker.runOperation(op);

        File newFile = op.getNewFile().toFile();
        XPkgMutableResource parent = (XPkgMutableResource) fileVar.getParentResource();
        XPkgFile newFileVar = new XPkgFile(newFile, parent);
        context.setVar(assigneeVarName, newFileVar);
    }
}
