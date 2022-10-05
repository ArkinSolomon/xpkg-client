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
 * This exception is thrown when a user tries to create a file or directory that already exists.
 */
public class XPkgFileExistsException extends XPkgRuntimeException {

    /**
     * Create a new exception saying that the file already exists.
     *
     * @param file The file that exists.
     */
    public XPkgFileExistsException(File file) {
        this(file, null);
    }

    /**
     * Create a new exception saying that the file already exists, that was caused by another exception.
     *
     * @param file The file that exists.
     * @param e    The exception that caused this exception.
     */
    public XPkgFileExistsException(File file, Throwable e) {
        super("The file or directory at '" + file.getAbsolutePath() + "' can not be created as it already exists", e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgFileExistsException(int line, XPkgFileExistsException e) {
        super("Error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgFileExistsException setLine(int line) {
        return new XPkgFileExistsException(line, this);
    }

}
