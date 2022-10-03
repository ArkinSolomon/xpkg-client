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

/**
 * This command sets a string variable's value.
 */
public class SetstrCommand extends Command {

    /**
     * The class execution command.
     *
     * @param args    The arguments provided to the command.
     * @param context The execution context that this command is being executed in.
     * @throws XPkgArgLenException         Thrown if there are less than two arguments.
     * @throws XPkgInvalidVarNameException Thrown if there is an invalid variable name.
     * @throws XPkgInvalidCallException    Thrown if the provided execution context is closed.
     * @throws XPkgImmutableVarException   Thrown if the user tries to set the value of a default environment variable.
     * @throws XPkgTypeMismatchException   Thrown if the user provides a variable instead of a ...STRING. In this case the {@link net.xpkgclient.commands.SetCommand} command should be used.
     */
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