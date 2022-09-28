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

//This variable type is a boolean, true or false
public class XPkgBool extends XPkgVar {

    //The data that this is storing
    private boolean data;

    //Create a new boolean
    public XPkgBool(boolean value) {
        data = value;
    }

    @Override
    public VarType getVarType() {
        return VarType.BOOL;
    }

    @Override
    public String toString() {
        return data ? "TRUE" : "FALSE";
    }

    @Override
    public XPkgBool copy() {
        return new XPkgBool(getValue());
    }

    public boolean getValue() {
        return data;
    }

    //Get or set the value
    public void setValue(boolean newValue) {
        data = newValue;
    }
}
