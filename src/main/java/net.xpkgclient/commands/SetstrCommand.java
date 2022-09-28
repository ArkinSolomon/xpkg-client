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
import net.xpkgclient.exceptions.XPkgArgLenException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.vars.XPkgString;

import java.util.Arrays;

//This command just sets the value of a variable to a string
public class SetstrCommand extends Command {
    public static void execute(String[] args, ExecutionContext context)
            throws XPkgArgLenException, XPkgInvalidVarNameException, XPkgInvalidCallException,
            XPkgImmutableVarException, XPkgTypeMismatchException {

        // Make sure there are arguments
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.SETSTR, 2, args.length);

        // Get the assigned variable
        String assignee = args[0];
        if (!ParseHelper.isValidVarName(assignee))
            throw new XPkgInvalidVarNameException(assignee);
        args = Arrays.copyOfRange(args, 1, args.length);

        if (args[0].startsWith("$"))
            throw new XPkgTypeMismatchException(CommandName.SETSTR, "second");

        // Make a new string
        XPkgString newStr = new XPkgString(String.join(" ", args));
        context.setVar(assignee, newStr);
    }
}