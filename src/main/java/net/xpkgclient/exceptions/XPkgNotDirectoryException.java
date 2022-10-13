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
 * Exception thrown if we expected a directory, but got a file instead.
 */
public class XPkgNotDirectoryException extends XPkgScriptRuntimeException {

    /**
     * Say the item at the specified was not a directory when it was expected to be.
     *
     * @param file The item that is not a directory.
     */
    public XPkgNotDirectoryException(File file) {
        this(file, null);
    }

    /**
     * Say the item at the specified was not a directory when it was expected to be, and that this exception was caused by another one.
     *
     * @param file The item that is not a directory.
     * @param e    The exception that caused this one.
     */
    public XPkgNotDirectoryException(File file, Throwable e) {
        super("Item is not directory: The item at '" + file.getAbsolutePath() + "' is not a directory as expected", e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgNotDirectoryException(int line, XPkgNotDirectoryException e) {
        super("Error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgNotDirectoryException setLine(int line) {
        return new XPkgNotDirectoryException(line, this);
    }
}
