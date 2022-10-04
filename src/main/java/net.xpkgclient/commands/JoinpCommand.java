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
import net.xpkgclient.exceptions.XPkgNotPathLikeException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.vars.VarType;
import net.xpkgclient.vars.XPkgString;
import net.xpkgclient.vars.XPkgVar;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * This class joins two pathlike strings and stores the result.
 */
class JoinpCommand extends Command {

    /**
     * The class execution command.
     *
     * @param args    The arguments to the command. See the readme for valid arguments.
     * @param context The execution context that this command executes within.
     * @throws XPkgException Can be thrown for multiple reasons such as user error, or a type mismatch, or another reason.
     */
    public static void execute(String @NotNull [] args, ExecutionContext context) throws XPkgException {

        // Check arguments
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.JOINP, 2, args.length);

        // Check if the variable is valid
        if (!ParseHelper.isValidVarName(args[0]))
            throw new XPkgInvalidVarNameException(args[0]);
        String assignee = args[0];
        if (!context.hasVar(args[0]))
            context.setVar(assignee, new XPkgString(""));
        else {

            // Make sure the variable is a string and valid
            XPkgVar assigneeVar = context.getVar(assignee);
            VarType assigneeType = assigneeVar.getVarType();
            if (assigneeType != VarType.STRING)
                throw new XPkgTypeMismatchException(CommandName.JOINP, "first", VarType.STRING, assigneeType);

            String assigneePath = ((XPkgString) assigneeVar).getValue();
            if (!ParseHelper.isValidPath(assigneePath))
                throw new XPkgNotPathLikeException(CommandName.JOINP, assignee, assigneePath);
        }

        // Get the second part of the path and combine them
        String secondHalf;
        args = Arrays.copyOfRange(args, 1, args.length);
        try {
            secondHalf = ParseHelper.getStr(args, context);
        } catch (XPkgInternalException e) {
            throw QuickHandles.handleGetStr(CommandName.JOINP, e);
        }
        if (!ParseHelper.isValidPath(secondHalf))
            throw new XPkgNotPathLikeException(CommandName.JOINP, secondHalf);


        XPkgString assigneeVar = (XPkgString) context.getVar(assignee);
        Path newPath = Paths.get(assigneeVar.getValue(), secondHalf);
        XPkgString newStr = new XPkgString(newPath.toString());
        context.setVar(assignee, newStr);
    }
}
