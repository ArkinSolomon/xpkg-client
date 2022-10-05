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

package net.xpkgclient.vars;

import java.io.File;

public class XPkgMutableResource extends XPkgResource {

    /**
     * Create a new mutable resource variable pointing to a directory.
     *
     * @param file The directory which this mutable resource points to.
     */
    public XPkgMutableResource(File file) {
        super(file);
    }

    /**
     * Get the type of variable this is.
     *
     * @return Always returns {@link VarType#MUTABLERESOURCE}.
     */
    @Override
    public VarType getVarType() {
        return VarType.MUTABLERESOURCE;
    }

    /**
     * True if this resource is mutable.
     *
     * @return Always returns true for this class.
     */
    @Override
    public boolean isMutable() {
        return true;
    }
}
