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
import net.xpkgclient.vars.XPkgBool;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Determine if a string can be considered pathlike, and store the result in a variable.
 */
final class IsplCommand extends Command {

    /**
     * The class execution command.
     *
     * @param args    The arguments to the command. See the readme for valid arguments.
     * @param context The execution context that this command executes within.
     * @throws XPkgException Can be thrown for multiple reasons such as user error, or a type mismatch, or another reason.
     */
    public static void execute(String @NotNull [] args, ExecutionContext context)
            throws
            XPkgException {

        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.ISPL, 2, args.length);

        String assignee = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        if (!ParseHelper.isValidVarName(assignee))
            throw new XPkgInvalidVarNameException(assignee);

        String testStr;
        try {
            testStr = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.ISPL, e);
        }

        boolean isValid = ParseHelper.isValidPath(testStr);
        context.setVar(assignee, new XPkgBool(isValid));
    }
}
