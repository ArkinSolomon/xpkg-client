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

// Top level exception for errors that occur when running scripts
public class XPkgScriptRunnerException extends XPkgException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -4155621685868796790L;

    // Basic constructors (basically the same as Exception)
    public XPkgScriptRunnerException() {
        this((String) null);
    }

    public XPkgScriptRunnerException(String message) {
        super(message);
    }

    public XPkgScriptRunnerException(String message, Exception cause) {
        super(message, cause);
    }

    public XPkgScriptRunnerException(Exception cause) {
        super(cause);
    }
}
