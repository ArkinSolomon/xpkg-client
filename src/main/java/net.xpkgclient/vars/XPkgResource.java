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

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A variable which represents a resource.
 */
public class XPkgResource extends XPkgVar {

    // The resource directory which this variable is pointing to.
    private File resourcePath;

    /**
     * Create a new resource variable pointing to a directory.
     *
     * @param file The directory which this resource points to.
     */
    public XPkgResource(File file) {
        resourcePath = file;
    }

    /**
     * Get the type of variable this is.
     *
     * @return Always returns {@link VarType#RESOURCE}.
     */
    @Override
    public VarType getVarType() {
        return VarType.RESOURCE;
    }

    /**
     * Get the string representation of the data held in the variable.
     *
     * @return The absolute path to the directory that this resource is pointing to.
     */
    @Override
    public String toString() {
        return resourcePath.toString();
    }

    /**
     * Create a copy of this variable.
     *
     * @return A new {@link XPkgResource} that points to the same directory as this one.
     */
    @Override
    public XPkgResource copy() {
        return new XPkgResource(resourcePath);
    }

    /**
     * Get the current value of this variable.
     *
     * @return The current value of this variable.
     */
    public File getValue() {
        return resourcePath;
    }

    /**
     * Update the value of this variable.
     *
     * @param newDir The path to the directory that this variable will point to.
     */
    public void setValue(@NotNull File newDir) {
        resourcePath = newDir;
    }
}
