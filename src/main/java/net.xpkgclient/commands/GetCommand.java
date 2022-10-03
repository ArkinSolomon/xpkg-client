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
import org.jetbrains.annotations.NotNull;

//TODO this

/**
 * Get a resource? idk yet
 */
class GetCommand extends Command {

    public static void execute(String @NotNull [] args, ExecutionContext context) {
        System.out.println("GET COMMAND EXECUTION: " + args[0] + ", " + args[1]);
        //
        //		// Make sure args[0] is a valid variable
        //		if (!ParseHelper.isValidVarName(args[0]))
        //			throw new XPkgTypeMismatchException(CommandName.GET, 'first', , g);
        //
        //		// Create the new variable or get it
        //		XPkgString newVar = new XPkgString(args[1] + "_AS_RESOURCE");
        //		context.setVar(args[0], newVar);
    }
}
