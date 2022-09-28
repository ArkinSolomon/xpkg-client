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

// This exception is called when a method is called that shouldn't be
public class XPkgInvalidCallException extends XPkgExecutionException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -7391328873326455824L;

    // Constructor that makes a message
    public XPkgInvalidCallException(String reason) {
        this(reason, 1);
    }

    public XPkgInvalidCallException(String reason, int index) {
        super("Invalid call to method '" + getCallerMethodName(index) + "': " + reason);
    }

    //Get the name of the method in the call stack
    private static String getCallerMethodName(int i) {
        return StackWalker.
                getInstance().
                walk(stream -> stream.skip(1).findFirst().get()).
                getMethodName();
    }
}
