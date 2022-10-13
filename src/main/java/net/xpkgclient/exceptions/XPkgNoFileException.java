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
 * Exception thrown when we expect file or directory to exist, but it doesn't.
 */
public class XPkgNoFileException extends XPkgScriptRuntimeException {

    /**
     * Say the file or directory was not found.
     *
     * @param file The file or directory that was not found.
     */
    public XPkgNoFileException(File file) {
        this(file, null);
    }

    /**
     * Say the file or directory was not found, and that this exception was caused by another one.
     *
     * @param file The file or directory that was not found.
     * @param e    The exception that caused this one.
     */
    public XPkgNoFileException(File file, Throwable e) {
        super("File does not exist: The file or directory at '" + file.getAbsolutePath() + "' can not be accessed as it doesn't exist", e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgNoFileException(int line, XPkgNoFileException e) {
        super("Error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgNoFileException setLine(int line) {
        return new XPkgNoFileException(line, this);
    }
}
