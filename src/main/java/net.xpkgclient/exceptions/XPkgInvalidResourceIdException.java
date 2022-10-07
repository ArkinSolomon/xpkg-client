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
 * Exception thrown when a command expects a resource id but got an invalid one.
 */
public class XPkgInvalidResourceIdException extends XPkgRuntimeException {

    /**
     * Say that a resource id was invalid and that a command expected it as a certain argument.
     *
     * @param cmd             The command that is throwing this exception.
     * @param argC            The ordinal index (first, second, third, fourth, etc.) of the argument which had a type mismatch.
     * @param invalidResource The resource id that was invalid.
     */
    public XPkgInvalidResourceIdException(CommandName cmd, String argC, String invalidResource) {
        this(cmd, argC, invalidResource, null);
    }

    /**
     * Say that a resource id was invalid and that a command expected it as a certain argument, and that this exception was caused by another one.
     *
     * @param cmd             The command that is throwing this exception.
     * @param argC            The ordinal index (first, second, third, fourth, etc.) of the argument which had a type mismatch.
     * @param invalidResource The resource id that was invalid.
     * @param e               The exception that caused this one.
     */
    public XPkgInvalidResourceIdException(CommandName cmd, String argC, String invalidResource, Throwable e) {
        super("Invalid resource id: The command '" + cmd + "' expected the " + argC + " argument to be a resource id, but instead got the invalid resource id '" + invalidResource + "'");
    }


    /**
     * Say that a resource id was invalid.
     *
     * @param invalidResource The invalid resource id.
     */
    public XPkgInvalidResourceIdException(String invalidResource) {
        this("Invalid resource id: The resource id '" + invalidResource + "' is invalid", null);
    }

    /**
     * Say that a resource id was invalid and that this exception as caused by another.
     *
     * @param invalidResource The invalid resource id.
     * @param e               The exception that caused this one.
     */
    public XPkgInvalidResourceIdException(String invalidResource, Throwable e) {
        super("Invalid resource id: The resource id '" + invalidResource + "'", e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgInvalidResourceIdException(int line, XPkgInvalidResourceIdException e) {
        super("Error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgInvalidResourceIdException setLine(int line) {
        return new XPkgInvalidResourceIdException(line, this);
    }
}
