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

import java.io.File;

/**
 * Exception thrown when trying to modify a file or resource that is not mutable.
 */
public class XPkgNotMutableException extends XPkgScriptRuntimeException {

    /**
     * Create a new exception saying that a file is not mutable.
     *
     * @param file The file that is not mutable.
     */
    public XPkgNotMutableException(File file){
    this(file, null);
    }

    /**
     * Create a new exception saying that a file is not mutable, and that another exception caused this one.
     *
     * @param file The file that is not mutable.
     * @param e The exception that threw this one.
     */
    public XPkgNotMutableException(File file, Throwable e){
        super("Not mutable: The file or directory at '" + file.getAbsolutePath() + "' is not mutable", e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgNotMutableException(int line, XPkgNotMutableException e) {
        super("Runtime error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgNotMutableException setLine(int line) {
        return new XPkgNotMutableException(line, this);
    }
}
