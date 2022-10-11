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

package net.xpkgclient.filesystem;

import java.io.IOException;

/**
 * An instance of this class is an operation done on the file system which can be undone.
 */
abstract class Operation {

    protected boolean performed;

    /**
     * Default constructor.
     */
    protected Operation() {
        performed = false;
    }

    /**
     * Perform the operation.
     */
    public abstract void perform() throws IOException;

    /**
     * Undo the operation.
     */
    public abstract void undo() throws IOException;
}
