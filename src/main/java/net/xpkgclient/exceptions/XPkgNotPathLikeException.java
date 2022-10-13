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
 * Exception thrown if a variable or string is not a pathlike when a pathlike variable or string was expected.
 */
public class XPkgNotPathLikeException extends XPkgScriptRuntimeException {

    /**
     * Create a new exception stating that a variable's value was not pathlike.
     *
     * @param cmd The command that is throwing this exception.
     * @param varName The name of the variable which held the invalid value.
     * @param value The value that was invalid (useful for the script author to debug).
     */
    public XPkgNotPathLikeException(CommandName cmd, String varName, String value) {
        super("Not pathlike:" + cmd + " expected the variable '" + varName + "' to be a pathlike string, got: " + value);
    }

    /**
     * Say a constant was not pathlike.
     *
     * @param cmd The command that is throwing this exception.
     * @param value The value that was invalid.
     */
    public XPkgNotPathLikeException(CommandName cmd, String value) {
        super("Not pathlike:" + cmd + " expected a pathlike, got: " + value);
    }


    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgNotPathLikeException(int line, XPkgNotPathLikeException e) {
        super("Runtime error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgNotPathLikeException setLine(int line) {
        return new XPkgNotPathLikeException(line, this);
    }
}
