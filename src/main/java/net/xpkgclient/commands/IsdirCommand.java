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
import net.xpkgclient.exceptions.XPkgException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgFile;

/**
 * This command determines if a file is existent and directory.
 */
final class IsdirCommand extends TwoVarCommand {

    /**
     * The command execution method.
     *
     * @param args    The arguments to the command. See the readme for valid arguments.
     * @param context The execution context that this command executes within.
     * @throws XPkgException Can be thrown for multiple reasons such as user error, or a type mismatch, or another reason.
     */
    public static void execute(String[] args, ExecutionContext context) throws XPkgException {
        updateValues(CommandName.ISDIR, args, context);

        if (!(secondArg instanceof XPkgFile fileVar))
            throw new XPkgTypeMismatchException(CommandName.ISDIR, "second", VarType.FILE, secondArg.getVarType());
        context.setVar(assigneeName, new XPkgBool(fileVar.exists() && fileVar.isDirectory()));
    }
}
