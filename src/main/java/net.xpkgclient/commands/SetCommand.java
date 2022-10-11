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
import net.xpkgclient.exceptions.XPkgExecutionException;
import net.xpkgclient.exceptions.XPkgImmutableVarException;
import net.xpkgclient.exceptions.XPkgInvalidCallException;
import net.xpkgclient.exceptions.XPkgInvalidVarNameException;
import net.xpkgclient.exceptions.XPkgParseException;
import net.xpkgclient.exceptions.XPkgTypeMismatchException;
import net.xpkgclient.exceptions.XPkgUndefinedVarException;
import net.xpkgclient.vars.XPkgBool;
import net.xpkgclient.vars.XPkgVar;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * This command sets a variable to another variable or the result of a boolean expression.
 */
final class SetCommand extends Command {

    /**
     * Set a variable to another variable or the result of a boolean expression.
     *
     * @param args    The arguments provided to the command.
     * @param context The execution context that this command is being executed in.
     * @throws XPkgParseException        Thrown if there was an error parsing the script, which can be a result of many user errors.
     * @throws XPkgUndefinedVarException Thrown if a variable provided by the user is not defined.
     * @throws XPkgInvalidCallException  Thrown if the execution context is closed.
     * @throws XPkgImmutableVarException Thrown if the user tries to overwrite a default variable.
     * @throws XPkgTypeMismatchException Thrown if there is a type mismatch within a boolean expression.
     */
    public static void execute(String @NotNull [] args, ExecutionContext context) throws XPkgParseException,
            XPkgUndefinedVarException, XPkgExecutionException, XPkgImmutableVarException,
            XPkgTypeMismatchException {

        // Make sure there are enough arguments
        if (args.length < 2)
            throw new XPkgArgLenException(CommandName.SET, 2, args.length);

        // Get the assigned variable
        String assignee = args[0];
        if (!ParseHelper.isValidVarName(assignee))
            throw new XPkgInvalidVarNameException(assignee);
        args = Arrays.copyOfRange(args, 1, args.length);

        // If it's a variable just copy it
        if (args.length == 1 && ParseHelper.isValidVarName(args[0])) {

            // Make sure the variable exists
            if (!context.hasVar(args[0]))
                throw new XPkgUndefinedVarException(args[0]);

            XPkgVar originalVar = context.getVar(args[0]);
            XPkgVar newVar = originalVar.copy();
            context.setVar(assignee, newVar);
            return;
        }

        // Set the variable to true if it evaluates to true
        context.setVar(assignee, new XPkgBool(ParseHelper.isTrue(args, context)));

    }
}