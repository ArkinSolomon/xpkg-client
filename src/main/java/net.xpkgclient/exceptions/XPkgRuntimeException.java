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

// Top level exception for errors that occur while running
public class XPkgRuntimeException extends XPkgScriptRunnerException implements ILineException<XPkgRuntimeException> {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -8557642804365998541L;

    // Class constructors (same as superclass constructors)
    public XPkgRuntimeException() {
    }

    public XPkgRuntimeException(String message) {
        super(message);
    }

    public XPkgRuntimeException(String message, Exception cause) {
        super(message, cause);
    }

    public XPkgRuntimeException(Exception cause) {
        super(cause);
    }

    private XPkgRuntimeException(int line, XPkgRuntimeException e) {
        this("Error at line " + line + ": " + e.getMessage(), e);
    }

    // Add the line to the message
    @Override
    public XPkgRuntimeException setLine(int line) {
        return new XPkgRuntimeException(line, this);
    }

}
