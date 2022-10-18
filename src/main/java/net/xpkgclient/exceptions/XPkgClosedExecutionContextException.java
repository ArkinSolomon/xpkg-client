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

import net.xpkgclient.packagemanager.ExecutionContext;

/**
 * Exception thrown when trying to perform actions on a closed {@link ExecutionContext}. Should only originate within the {@code activityCheck()} method of {@link ExecutionContext}.
 */
public class XPkgClosedExecutionContextException extends XPkgInvalidCallException {

    /**
     * Default constructor with a default message saying the execution context has been closed.
     */
    public XPkgClosedExecutionContextException() {
        super("Execution context has been closed and is not active", 4);
    }
}
