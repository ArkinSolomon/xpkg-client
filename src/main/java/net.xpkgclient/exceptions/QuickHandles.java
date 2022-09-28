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

package net.xpkgclient.exceptions;

import net.xpkgclient.commands.CommandName;
import net.xpkgclient.vars.VarType;

// This class provides static functions for quick exception handling of internal exceptions
public class QuickHandles {

    // Methods to handle ParseHelper.getStr();
    public static XPkgException handleGetStr(CommandName cmd, XPkgInternalException e) {
        if (e.getMessage().equalsIgnoreCase("arglen"))
            return new XPkgArgLenException(cmd, 2, "a variable is the second argument");
        else if (e.getMessage().equalsIgnoreCase("mismatch"))
            return new XPkgTypeMismatchException(cmd, "second", VarType.STRING, (VarType) e.getData(), e);
        else
            return new XPkgException("An unknown exception occurred", e);
    }
}
