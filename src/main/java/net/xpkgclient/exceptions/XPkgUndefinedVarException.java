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

/**
 * Exception thrown when a variable is undefined.
 */
public class XPkgUndefinedVarException extends XPkgScriptRuntimeException {

    /**
     * Say that a variable is undefined.
     *
     * @param varName The name of the variable that was undefined.
     */
    public XPkgUndefinedVarException(String varName) {
        this("Undefined variable: '" + varName + "' is not defined", null);
    }

    /**
     * Say that a variable is undefined, and that this exception was caused by another one.
     *
     * @param varName The name of the variable that was undefined.
     * @param cause The exception that caused this one.
     */
    public XPkgUndefinedVarException(String varName, Throwable cause) {
        super("Undefined variable: '" + varName + "' is not defined", cause);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgUndefinedVarException(int line, XPkgUndefinedVarException e) {
        super("Error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgUndefinedVarException setLine(int line) {
        return new XPkgUndefinedVarException(line, this);
    }
}
