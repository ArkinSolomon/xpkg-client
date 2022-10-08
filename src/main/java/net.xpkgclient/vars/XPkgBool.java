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

/**
 * A boolean (true or false) variable.
 */
public class XPkgBool extends XPkgVar {

    private final boolean data;

    /**
     * Create a new boolean variable with a value.
     *
     * @param value The value to set the new variable to.
     */
    public XPkgBool(boolean value) {
        data = value;
    }

    /**
     * Get the type of variable this is.
     *
     * @return Always returns {@link VarType#BOOL}.
     */
    @Override
    public VarType getVarType() {
        return VarType.BOOL;
    }

    /**
     * Get the string representation of the data held in the variable.
     *
     * @return "TRUE" if the value of the variable is true, or "FALSE" if it is not.
     */
    @Override
    public String toString() {
        return data ? "TRUE" : "FALSE";
    }

    /**
     * Create a copy of this variable.
     *
     * @return A new {@link XPkgBool} with the same value as this variable.
     */
    @Override
    public XPkgBool copy() {
        return new XPkgBool(data);
    }

    /**
     * Get the current value of this variable.
     *
     * @return The current value of this variable.
     */
    public boolean getValue() {
        return data;
    }
}
