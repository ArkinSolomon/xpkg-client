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

/**
 * Top level exception for all custom exceptions.
 */
public class XPkgException extends Exception {

    @Serial
    private static final long serialVersionUID = 5674756055664564736L;

    /**
     * Default constructor, creates a new exception with no message.
     */
    public XPkgException() {
        super();
    }

    /**
     * Create a new exception with a message.
     *
     * @param message The message for the exception.
     */
    public XPkgException(String message) {
        super(message);
    }

    /**
     * Create an exception with a message and a cause.
     *
     * @param message The message for the exception.
     * @param cause   The cause of the exception.
     */
    public XPkgException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a message with a cause and no message.
     *
     * @param cause The cause of the exception.
     */
    public XPkgException(Throwable cause) {
        super(cause);
    }
}