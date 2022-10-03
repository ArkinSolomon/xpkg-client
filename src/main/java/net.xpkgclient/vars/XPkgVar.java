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
 * Top level superclass which all variable types for XPKGS will inherit from.
 */
public abstract class XPkgVar {

    //Get the type of variable

    /**
     * Get the type of a variable, should be constant and hardcoded within the variable.
     *
     * @return The type of variable.
     */
    public abstract VarType getVarType();

    /**
     * Get a string representation of the variable, mostly for printing.
     *
     * @return The string representation of the variable.
     */
    public abstract String toString();

    /**
     * Create a copy of the variable.
     *
     * @return A variable of the same type and with the same value.
     */
    public abstract XPkgVar copy();
}	
