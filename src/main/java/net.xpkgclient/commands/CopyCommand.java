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
import net.xpkgclient.exceptions.XPkgArgLenException;
import org.jetbrains.annotations.NotNull;

/**
 * This command copies a file from one location to another.
 */
class CopyCommand extends Command {

    /**
     * The class execution command.
     *
     * @param args    The arguments to the command. There should not be any.
     * @param context The execution context to print.
     * @throws XPkgArgLenException Thrown if there are more than two arguments provided.
     */
    public static void execute(String @NotNull [] args, ExecutionContext context) throws XPkgArgLenException {
        if (args.length != 2)
            throw new XPkgArgLenException(CommandName.COPY, 2, args.length);
    }
}
