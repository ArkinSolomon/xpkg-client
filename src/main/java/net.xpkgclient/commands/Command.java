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
import net.xpkgclient.exceptions.XPkgExecutionException;
import net.xpkgclient.exceptions.XPkgUnimplementedException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * This class is the superclass of all specific commands, it stores all the commands.
 */
public class Command {

    // Store all commands
    private static final HashMap<CommandName, Method> cmds = new HashMap<>();

    // True if we've registered commands already
    private static boolean hasRegisteredCommands = false;

    /**
     * Register all commands.
     *
     * @throws XPkgExecutionException Thrown if a command does not have a static {@code execute} method.
     */
    public static void registerCommands() throws XPkgExecutionException {

        //Only register once
        if (hasRegisteredCommands)
            return;
        hasRegisteredCommands = true;

        //Register all commands
        registerCmd(CommandName.GET, GetCommand.class);
        registerCmd(CommandName.PRINT, PrintCommand.class);
        registerCmd(CommandName.SET, SetCommand.class);
        registerCmd(CommandName.SETSTR, SetstrCommand.class);
        registerCmd(CommandName.MKDIR, MkdirCommand.class);
        registerCmd(CommandName.MKDIRS, MkdirsCommand.class);
        registerCmd(CommandName.ISPL, IsplCommand.class);
        registerCmd(CommandName.JOIN, JoinCommand.class);
        registerCmd(CommandName.JOINP, JoinpCommand.class);
        registerCmd(CommandName.CONTEXT, ContextCommand.class);
        registerCmd(CommandName.COPY, CopyCommand.class);
        registerCmd(CommandName.POINT, PointCommand.class);
        registerCmd(CommandName.RESOLVE, ResolveCommand.class);
    }

    /**
     * Register a single command.
     *
     * @param cName The name of the command to register.
     * @param c     The reference to the command class type.
     * @throws XPkgExecutionException Thrown if a command does not have a static {@code execute} method, or if there was a security exception.
     */
    private static void registerCmd(CommandName cName, Class<? extends Command> c) throws XPkgExecutionException {

        // Make sure command has an execute() method
        Method m;
        try {
            m = c.getMethod("execute", String[].class, ExecutionContext.class);
        } catch (SecurityException e) {
            throw new XPkgExecutionException(e);
        } catch (NoSuchMethodException e) {
            throw new XPkgUnimplementedException("'" + c.getName() + "' does not have a valid execute() method");
        }

        // Make sure this isn't a duplicate
        if (cmds.containsKey(cName))
            throw new XPkgExecutionException("Attempted to register command '" + cName + "' more than once");

        cmds.put(cName, m);
    }

    /**
     * Execute a single command.
     *
     * @param cName   The name of the command to execute.
     * @param args    The arguments provided to the command.
     * @param context The context to execute the command in.
     * @throws Throwable An {@code XPkgUnimplementedException} if the command can be thrown if the command isn't implemented. It also will pass up any exceptions thrown by the command itself.
     */
    public static void call(CommandName cName, String[] args, ExecutionContext context) throws Throwable {
        Method m = cmds.get(cName);
        if (m == null)
            throw new XPkgUnimplementedException("The command '" + cName
                    + "' hasn't been registered or is an improperly handled flow control statement");

        // Pass the exceptions up to main
        try {
            m.invoke("execute", args, context);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
