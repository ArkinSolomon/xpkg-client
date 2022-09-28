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

// Exceptions meant to be caught in order to be thrown later (useful for full
// stack trace debugging), used mainly in static classes and passing
// information
public class XPkgInternalException extends Exception {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -6371838915978668117L;

    Object data;

    // Basic constructors
    public XPkgInternalException() {
        this(null, null);
    }

    public XPkgInternalException(String message) {
        this(message, null);
    }

    public XPkgInternalException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public XPkgInternalException(String message, Exception cause) {
        super(message, cause);
    }

    // We want to call our own constructor to set this.message
    public XPkgInternalException(Exception cause) {
        this(cause == null ? null : cause.toString(), cause);
        if (cause instanceof XPkgInternalException)
            data = ((XPkgInternalException) cause).getData();
    }

    // Get the data passed in
    public Object getData() {
        return data;
    }
}
