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

/**
 * Exception thrown when trying to get an item has not been set.
 */
public class XPkgNotSetException extends XPkgInvalidCallException {

    //TODO check the index of the callstack
    /**
     * Create a simple exception saying the variable has not been set.
     *
     * @param var The variable that hasn't been set.
     */
    public XPkgNotSetException(String var) {
        super("Variable not set: '" + var + "' has not been set", 4);
    }
}
