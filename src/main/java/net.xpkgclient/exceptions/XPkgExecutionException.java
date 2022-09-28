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

// Top level exception for exceptions that are not the script programmers fault, but rather an error with the OS or XPkg
public class XPkgExecutionException extends XPkgScriptRunnerException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = 3113973855303520458L;

    // Class constructors (same as superclass's)
    public XPkgExecutionException() {
    }

    public XPkgExecutionException(String message) {
        super(message);
    }

    public XPkgExecutionException(String message, Exception cause) {
        super(message, cause);
    }

    public XPkgExecutionException(Exception cause) {
        super(cause);
    }
}
