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

//This class represents a resource 
public class XPkgResource extends XPkgVar {

    // The resource file
    private File resourcePath;

    // Create a new resource variable pointing to the file
    public XPkgResource(File path) {
        resourcePath = path;
    }

    @Override
    public VarType getVarType() {
        return VarType.RESOURCE;
    }

    @Override
    public String toString() {
        return resourcePath.toString();
    }

    @Override
    public XPkgResource copy() {
        return new XPkgResource(resourcePath);
    }

    public File getValue() {
        return resourcePath;
    }

    // Getter and setter for the path
    public void setValue(File path) {
        resourcePath = path;
    }
}
