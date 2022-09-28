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

//Exception thrown when the execution context is closed
public class XPkgClosedExecutionContextException extends XPkgInvalidCallException {

    //Serial identifier
    @Serial
    private static final long serialVersionUID = 813342998451479620L;

    //Use a default message
    public XPkgClosedExecutionContextException() {
        super("Execution context has been closed and is not active");
    }
}
