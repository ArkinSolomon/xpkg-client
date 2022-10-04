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
 * Top level exception for exceptions that are not the script programmers fault, but rather an error with X-Pkg.
 */
public class XPkgExecutionException extends XPkgScriptRunnerException {

    @Serial
    private static final long serialVersionUID = 3113973855303520458L;

    /**
     * Default constructor. Create a new exception with no message.
     */
    public XPkgExecutionException() {
        super();
    }

    /**
     * Create a new exception with a message.
     *
     * @param message The message for the exception.
     */
    public XPkgExecutionException(String message) {
        super(message);
    }

    /**
     * Create a new exception with a message and a cause.
     *
     * @param message The message for the exception.
     * @param cause   The cause of the exception.
     */
    public XPkgExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new exception with a cause and no message.
     *
     * @param cause The cause of the exception.
     */
    public XPkgExecutionException(Throwable cause) {
        super(cause);
    }
}
