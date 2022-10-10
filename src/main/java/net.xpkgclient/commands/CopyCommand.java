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
import net.xpkgclient.exceptions.QuickHandles;
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgInternalException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.filesystem.CopyFileOperation;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgFile;
import net.xpkgclient.vars.XPkgMutableResource;
import net.xpkgclient.vars.XPkgVar;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * This command copies a file from one location to another.
 */
class CopyCommand extends Command {

    /**
     * The class execution command.
     *
     * @param args    The arguments to the command. There should not be any.
     * @param context The execution context to print.
     * @throws XPkgArgLenException Thrown if there are more than two arguments provided.
     */
    public static void execute(String @NotNull [] args, ExecutionContext context) throws XPkgException, IOException {
        if (args.length != 2)
            throw new XPkgArgLenException(CommandName.COPY, 2, args.length);

        String fileVarName = args[0];
        String mutResVarName = args[1];
        String argC = "first";
        XPkgVar unknownTFileVar, unknownTMutResVar;
        try {
            unknownTFileVar = context.checkExistingVar(fileVarName);
            argC = "second";
            unknownTMutResVar = context.checkExistingVar(mutResVarName);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleCheckExistingVar(CommandName.COPY, argC, e);
        }

        if (unknownTFileVar.getVarType() != VarType.FILE)
            throw new XPkgTypeMismatchException(CommandName.COPY, "first", VarType.FILE, unknownTFileVar.getVarType());
        if (unknownTMutResVar.getVarType() != VarType.MUTABLERESOURCE)
            throw new XPkgTypeMismatchException(CommandName.COPY, "second", VarType.MUTABLERESOURCE, unknownTFileVar.getVarType());
        XPkgFile fileVar = (XPkgFile) unknownTFileVar;
        XPkgMutableResource mutVar = (XPkgMutableResource) unknownTMutResVar;

        File destDirectory = mutVar.getValue();
        context.fileTracker.runOperation(new CopyFileOperation(fileVar, destDirectory));
    }
}
