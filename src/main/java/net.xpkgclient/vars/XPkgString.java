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

public class XPkgString extends XPkgVar {

    // The data that this variable stores
    private String data;

    // Create a new string
    public XPkgString(String value) {
        data = value;
    }

    @Override
    public VarType getVarType() {
        return VarType.STRING;
    }

    @Override
    public String toString() {
        return data;
    }

    @Override
    public XPkgString copy() {
        return new XPkgString(getValue());
    }

    public String getValue() {
        return data;
    }

    // Get or set the string
    public void setValue(String newValue) {
        data = newValue;
    }
}
