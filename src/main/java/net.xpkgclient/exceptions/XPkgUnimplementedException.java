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

// An exception thrown when a class or method is not registered or implemented
public class XPkgUnimplementedException extends XPkgExecutionException {

    //Serial identifier
    @Serial
    private static final long serialVersionUID = -3064139511429346241L;

    // Provide a message
    public XPkgUnimplementedException(String message) {
        super("Unimplemented exception [not a script issue, but a problem with X-Pkg]:" + message);
    }
}
