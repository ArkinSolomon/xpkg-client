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

import java.util.Arrays;

/**
 * Determine if a string can be considered pathlike, and store the result in a variable.
 */
public class IsplCommand extends Command {

    /**
     * The class execution command.
     *
     * @param args    The arguments to the command. See the readme for valid arguments.
     * @param context The execution context that this command executes within.
     * @throws XPkgException Can be thrown for multiple reasons such as user error, or a type mismatch, or another reason.
     */
    public static void execute(String[] args, ExecutionContext context)
            throws
            XPkgException {

        // Argument checking
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.ISPL, 2, args.length);

        // Variable to assign the result to
        String assignee = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        // Make sure the assignee is valid
        if (!ParseHelper.isValidVarName(assignee))
            throw new XPkgInvalidVarNameException(assignee);

        // The string that might be a path
        String testStr;
        try {
            testStr = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.ISPL, e);
        }

        // Check the path name and set the value
        boolean isValid = ParseHelper.isValidPath(testStr);
        context.setVar(assignee, new XPkgBool(isValid));
    }
}
