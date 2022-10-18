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

// Used when argument lengths do not match up

/**
 * Thrown when a command expects a certain amount of arguments, but either gets too many or too few.
 */
public class XPkgArgLenException extends XPkgParseException {

    /**
     * Constructor which is used to throw when a command expects a fixed amount of arguments.
     *
     * @param cmd      The command that threw the exception.
     * @param expected The expected amount of arguments.
     * @param actual   The amount of arguments the command had.
     */
    public XPkgArgLenException(CommandName cmd, int expected, int actual) {
        super("Invalid argument length: The " + cmd + " command expects " + expected + " arguments, got " + actual);
    }

    /**
     * Constructor which is used to throw when a command expects a maximum amount of arguments for a certain reason.
     *
     * @param cmd    The command that threw the exception.
     * @param max    The maximum amount of arguments for the given reason.
     * @param reason In what scenario the command expects the maximum (prepended with "if ").
     */
    public XPkgArgLenException(CommandName cmd, int max, String reason) {
        this(cmd, max, reason, null);
    }

    /**
     * Constructor which is used to throw when a command expects a maximum amount of arguments for a certain reason, with another exception as the cause.
     *
     * @param cmd    The command that threw the exception.
     * @param max    The maximum amount of arguments for the given reason.
     * @param reason In what scenario the command expects the maximum (prepended with "if ").
     * @param cause The exception that caused this one.
     */
    public XPkgArgLenException(CommandName cmd, int max, String reason, Throwable cause) {
        super("Invalid argument length: The " + cmd + " command expects a maximum of" + max + " arguments if " + reason, cause);
    }

    /**
     * Constructor which is used to throw when a command expects no arguments, but is given some anyway.
     *
     * @param cmd   The command that threw the exception.
     * @param given The amount of arguments that were given to the command.
     */
    public XPkgArgLenException(CommandName cmd, int given) {
        super("Invalid argument length: The " + cmd + " command takes no arguments, but " + given + " were given");
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgArgLenException(int line, XPkgArgLenException e) {
        super("Parse error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgArgLenException setLine(int line) {
        return new XPkgArgLenException(line, this);
    }
}
