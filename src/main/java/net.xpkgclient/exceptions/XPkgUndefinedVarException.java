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

import java.io.Serial;

//Exception thrown when a variable is undefined

/**
 * Exception thrown when a variable is undefined.
 */
public class XPkgUndefinedVarException extends XPkgRuntimeException {

    @Serial
    private static final long serialVersionUID = -2122679038771821443L;

    // Just say a variable is not defined

    /**
     * Say that a variable is undefined.
     *
     * @param varName The name of the variable that was undefined.
     */
    public XPkgUndefinedVarException(String varName) {
        super("Undefined variable: '" + varName + "' is not defined");
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgUndefinedVarException(int line, XPkgRuntimeException e) {
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
