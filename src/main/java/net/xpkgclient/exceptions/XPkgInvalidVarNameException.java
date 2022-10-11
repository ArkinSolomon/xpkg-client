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

package net.xpkgclient.exceptions;

import net.xpkgclient.commands.CommandName;

/**
 * Exception thrown when a variable name is invalid.
 */
public class XPkgInvalidVarNameException extends XPkgParseException {

    /**
     * Create a new exception with a simple message saying that a name is invalid for a variable.
     *
     * @param invalidVarName The name of the variable that is invalid.
     */
    public XPkgInvalidVarNameException(String invalidVarName) {
        super("Invalid variable name: '" + invalidVarName + "' is not a valid variable");
    }

    /**
     * Create a new exception saying that a command expected a variable as the nth argument, but instead got something else.
     *
     * @param cmd            The command that is throwing this exception.
     * @param argC           The ordinal index (first, second, third, fourth, etc.) of the argument which was supposed to be a variable.
     * @param invalidVarName The name of the variable that is invalid.
     */
    public XPkgInvalidVarNameException(CommandName cmd, String argC, String invalidVarName) {
        this(cmd, argC, invalidVarName, null);
    }

    /**
     * Create a new exception saying that a command expected a variable as the nth argument, but instead got something else, and that this exception was caused by another exception.
     *
     * @param cmd            The command that is throwing this exception.
     * @param argC           The ordinal index (first, second, third, fourth, etc.) of the argument which was supposed to be a variable.
     * @param invalidVarName The name of the variable that is invalid.
     * @param cause          The exception that caused this one.
     */
    public XPkgInvalidVarNameException(CommandName cmd, String argC, String invalidVarName, Throwable cause) {
        super("Invalid variable name: The " + cmd + " command expected a variable as the " + argC + " argument, but instead got '" + invalidVarName + "'", cause);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgInvalidVarNameException(int line, XPkgInvalidVarNameException e) {
        super("Parse error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgInvalidVarNameException setLine(int line) {
        return new XPkgInvalidVarNameException(line, this);
    }
}
