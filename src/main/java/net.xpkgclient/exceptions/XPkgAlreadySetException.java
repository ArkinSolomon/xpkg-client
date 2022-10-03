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
 * Exception thrown when a meta variable in an execution context has already not be set.
 */
public class XPkgAlreadySetException extends XPkgInvalidCallException {

    @Serial
    private static final long serialVersionUID = 7960672666236728584L;

    //TODO check the index of the callstack
    /**
     * Throw with a simple message.
     *
     * @param var The variable that has already been set.
     */
    public XPkgAlreadySetException(String var) {
        super("'" + var + "' has already been set", 6);
    }
}
