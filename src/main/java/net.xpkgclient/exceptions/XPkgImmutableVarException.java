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

// Exception when trying to change an immutable variable
public class XPkgImmutableVarException extends XPkgRuntimeException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -5842352310837071330L;

    // Say that the variable is immutable
    public XPkgImmutableVarException(String varName) {
        super("'" + varName + "' is immutable and can not be changed");
    }
}
