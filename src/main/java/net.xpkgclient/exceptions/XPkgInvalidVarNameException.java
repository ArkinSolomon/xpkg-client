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

import java.io.Serial;

/**
 * Exception thrown when a variable name is invalid.
 */
public class XPkgInvalidVarNameException extends XPkgParseException {

    @Serial
    private static final long serialVersionUID = -2027953150186197578L;

    /**
     * Create a new exception with a simple message saying that a name is invalid for a variable.
     *
     * @param name The name of the variable that is invalid.
     */
    public XPkgInvalidVarNameException(String name) {
        super("Invalid variable name: '" + name + "' is not a valid variable");
    }

    /**
     * Create a new exception saying that a command expected a variable as an argument.
     *
     * @param cmd  The command that is throwing this exception.
     * @param name The name of the variable that is invalid.
     */
    public XPkgInvalidVarNameException(CommandName cmd, String name) {
        super("Invalid variable name: The command '" + cmd + "' expected a variable as the first argument, but instead got '" + name + "'");
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
