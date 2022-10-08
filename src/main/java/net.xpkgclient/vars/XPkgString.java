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
 * A variable which represents a STRING.
 */
public class XPkgString extends XPkgVar {

    // The data that this variable stores
    private final String data;

    /**
     * Create a new variable with a value.
     *
     * @param value The initial value of this variable.
     */
    public XPkgString(String value) {
        data = value;
    }

    /**
     * Get the type of variable this is.
     *
     * @return Always returns {@link VarType#STRING}.
     */
    @Override
    public VarType getVarType() {
        return VarType.STRING;
    }

    /**
     * Returns the data in the variable. Since the data is a string() this does the same thing as {@link #getValue()}.
     *
     * @return The data in the variable.
     */
    @Override
    public String toString() {
        return data;
    }

    /**
     * Create a copy of this variable.
     *
     * @return A new {@link XPkgString} with the same string value as this one.
     */
    @Override
    public XPkgString copy() {
        return new XPkgString(data);
    }

    /**
     * Get the value of this variable.
     *
     * @return The data stored in this variable.
     */
    public String getValue() {
        return data;
    }
}
