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
 * This exception is thrown when a resource id is valid, but is not found on the remote server.
 */
public class XPkgNoResourceException extends XPkgRuntimeException {

    /**
     * Create a new exception saying that the resource doesn't exist.
     *
     * @param resourceId The id of the resource that doesn't exist.
     */
    public XPkgNoResourceException(String resourceId) {
        super("Resource not found: The resource '" + resourceId + "' could not be found");
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     * @param e    The exception to add the line to.
     */
    private XPkgNoResourceException(int line, XPkgNoResourceException e) {
        super("Error at line " + line + ": " + e.getMessage(), e);
    }

    /**
     * Add a line to the exception.
     *
     * @param line The line number at which the exception occurred.
     */
    @Override
    public XPkgNoResourceException setLine(int line) {
        return new XPkgNoResourceException(line, this);
    }
}
