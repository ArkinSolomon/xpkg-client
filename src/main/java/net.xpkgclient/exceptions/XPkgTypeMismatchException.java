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
import net.xpkgclient.vars.VarType;

import java.io.Serial;

/**
 * Exception thrown when the XPKGS script runner expects a certain type and gets another.
 */
public class XPkgTypeMismatchException extends XPkgRuntimeException {

    @Serial
    private static final long serialVersionUID = -3787777489114171239L;

    /**
     * Create a new exception with a message saying that a certain argument for a command required a certain type, but got a different one.
     *
     * @param command  The command that is throwing this exception.
     * @param argC     The ordinal index (first, second, third, fourth, etc.) of the argument which had a type mismatch.
     * @param expected The expected type of the variable.
     * @param actual   The actual type of the variable.
     */
    public XPkgTypeMismatchException(CommandName command, String argC, VarType expected, VarType actual) {
        this(command, argC, expected, actual, null);
    }

    /**
     * Create a new exception with a message saying that a certain argument for a command required a certain type, but got a different one, that was caused by another exception.
     *
     * @param command  The command that is throwing this exception.
     * @param argC     The ordinal index (first, second, third, fourth, etc.) of the argument which had a type mismatch.
     * @param expected The expected type of the variable.
     * @param actual   The actual type of the variable.
     * @param cause    The exception that caused this.
     */
    public XPkgTypeMismatchException(CommandName command, String argC, VarType expected, VarType actual, Exception cause) {
        super("The " + command + " command expected the " + argC + " argument to be " + expected + " but got " + actual
                + " instead", cause);
    }

    /**
     * Say that an argument of a command had to be a ...STRING type.
     *
     * @param command The command which required the second argument to be a ...STRING type.
     * @param argC    The ordinal index (first, second, third, fourth, etc.) of the argument which was expected to be a ...STRING.
     */
    public XPkgTypeMismatchException(CommandName command, String argC) {
        super("The " + command + " command requires the " + argC + " argument to be a ...STRING");
    }

    /**
     * Exception thrown when a variable was expected to be a certain type, but instead got a different type.
     *
     * @param varName  The variable that did not have the correct type.
     * @param expected The expected type of the variable.
     * @param actual   The actual type of the variable.
     */
    public XPkgTypeMismatchException(String varName, VarType expected, VarType actual) {
        this(varName, expected, actual, null);
    }

    /**
     * Exception thrown when a variable was expected to be a certain type, but instead got a different type, as a result of another exception.
     *
     * @param varName  The variable that did not have the correct type.
     * @param expected The expected type of the variable.
     * @param actual   The actual type of the variable.
     * @param cause    The exception that caused this exception.
     */
    public XPkgTypeMismatchException(String varName, VarType expected, VarType actual, Exception cause) {
        super("Variable '" + varName + "' was expected to be a " + expected + " but got " + actual + " instead", cause);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgTypeMismatchException(int line, XPkgTypeMismatchException e) {
        super("Error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgTypeMismatchException setLine(int line) {
        return new XPkgTypeMismatchException(line, this);
    }
}
