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
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgString;
import net.xpkgclient.vars.XPkgVar;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * This command joins two strings together and stores the result.
 */
class JoinCommand extends Command {

    /**
     * The class execution command.
     *
     * @param args    The arguments to the command. See the readme for valid arguments.
     * @param context The execution context that this command executes within.
     * @throws XPkgException Can be thrown for multiple reasons such as user error, or a type mismatch, or another reason.
     */
    public static void execute(String @NotNull [] args, ExecutionContext context) throws XPkgException {

        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.JOIN, 2, args.length);

        if (!ParseHelper.isValidVarName(args[0]))
            throw new XPkgInvalidVarNameException(args[0]);

        String assignee = args[0];
        if (!context.hasVar(assignee))
            context.setVar(assignee, new XPkgString(""));
        else {
            XPkgVar assigneeVar = context.getVar(assignee);
            VarType assigneeType = assigneeVar.getVarType();
            if (assigneeType != VarType.STRING)
                throw new XPkgTypeMismatchException(CommandName.JOIN, "first", VarType.STRING, assigneeType);
        }

        // Check if the second argument is a variable
        args = Arrays.copyOfRange(args, 1, args.length);

        // The string to add to the variable assignee
        String catString;
        try {
            catString = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.JOIN, e);
        }

        // Update the variable in memory
        XPkgString assigneeVar = (XPkgString) context.getVar(assignee);
        XPkgString newVar = new XPkgString(assigneeVar.getValue() + catString);
        context.setVar(assignee, newVar);
    }
}
