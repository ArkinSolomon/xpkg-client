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
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.vars.XPkgVar;
import org.jetbrains.annotations.NotNull;

/**
 * This is a top level command, used to easily parse for commands that only require an assignee, and a variable, where the first argument is the name of a variable, and the second argument is an existing variable.
 */
abstract class TwoVarCommand extends Command {
    protected static String assigneeName;
    protected static XPkgVar secondArg;

    /**
     * Update the value of the static variables.
     *
     * @param args    The arguments provided to the command.
     * @param context The execution context to get variables from.
     * @throws XPkgException Exception can be thrown due to user error, like argument length, or invalid variable names, or something else.
     */
    protected static void updateValues(CommandName name, String @NotNull [] args, ExecutionContext context) throws XPkgException {
        if (args.length != 2)
            throw new XPkgArgLenException(name, 2, args.length);

        assigneeName = args[0];
        if (!ParseHelper.isValidVarName(assigneeName))
            throw new XPkgInvalidVarNameException(name, "first", assigneeName);

        try {
            secondArg = context.checkExistingVar(args[1]);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleCheckExistingVar(name, "second", e);
        }
    }
}
