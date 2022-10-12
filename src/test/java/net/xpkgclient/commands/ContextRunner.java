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

package net.xpkgclient.commands;

import net.xpkgclient.ExecutionContext;
import net.xpkgclient.exceptions.XPkgException;

/**
 * This interface allows creating lambda expressions with a single execution context as the parameter.
 */
interface ContextRunner {

    /**
     * Run something in an execution context.
     *
     * @param context The execution context that the method will be run in.
     */
    void run(ExecutionContext context) throws XPkgException;
}
