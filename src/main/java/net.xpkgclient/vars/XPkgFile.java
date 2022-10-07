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

/**
 * A variable which represents a file, which can point to a file or directory.
 */
public class XPkgFile extends XPkgVar {

    private File file;

    /**
     * Create a new file variable pointing to a file.
     *
     * @param file The file that this variable points to initially.
     */
    public XPkgFile(File file) {
        this.file = file;
    }

    /**
     * Get the type of variable this is.
     *
     * @return Always returns {@link VarType#FILE}
     */
    @Override
    public VarType getVarType() {
        return VarType.FILE;
    }

    /**
     * Get the string representation of the data held in the variable.
     *
     * @return The absolute path to the file or directory that this variable is pointing to.
     */
    @Override
    public String toString() {
        return file.toString();
    }

    /**
     * Create a copy of this variable.
     *
     * @return A new {@link XPkgFile} that points to the same file or directory as this one.
     */
    @Override
    public XPkgFile copy() {
        return new XPkgFile(file);
    }

    /**
     * Get the current value of this variable.
     *
     * @return The {@link File} object of the file that this variable points to.
     */
    public File getValue() {
        return file;
    }

    /**
     * Set the current value of this variable.
     *
     * @param file The {@link File} object that the value this variable will point to.
     */
    public void setValue(File file) {
        this.file = file;
    }

    /**
     * Check if the value of this variable points to a directory or a file.
     *
     * @return True if this file points to a directory and false if it points to a file.
     */
    public boolean isDirectory() {
        return file.isDirectory();
    }
}
