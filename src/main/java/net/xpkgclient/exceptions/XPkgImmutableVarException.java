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

// Exception when trying to change an immutable variable

/**
 * Exception thrown when trying to change an immutable variable, which is one of the default/environment variables created when the {@link net.xpkgclient.ExecutionContext} is created.
 */
public class XPkgImmutableVarException extends XPkgScriptRuntimeException implements ILineException<XPkgScriptRuntimeException> {

    /**
     * Create a new exception that says a given variable name is immutable.
     *
     * @param varName The immutable variable.
     */
    public XPkgImmutableVarException(String varName) {
        super("'" + varName + "' is immutable and can not be changed");
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgImmutableVarException(int line, XPkgImmutableVarException e) {
        super("Parse error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgImmutableVarException setLine(int line) {
        return new XPkgImmutableVarException(line, this);
    }
}
