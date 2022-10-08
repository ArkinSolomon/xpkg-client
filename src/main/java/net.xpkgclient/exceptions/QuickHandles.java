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

import net.xpkgclient.ExecutionContext;
import net.xpkgclient.commands.CommandName;
import net.xpkgclient.vars.VarType;

/**
 * This class provides static methods for quick exception handling of internal exceptions that occur commonly.
 */
public class QuickHandles {

    /**
     * This static method handles any {@link net.xpkgclient.exceptions.XPkgInternalException} that is thrown by {@link net.xpkgclient.ParseHelper#getStr(String[], ExecutionContext)}.
     *
     * @param cmd The command that is handling the exception thrown.
     * @param e   The {@link net.xpkgclient.exceptions.XPkgInternalException} thrown.
     * @return A clear human-readable exception.
     */
    public static XPkgException handleGetStr(CommandName cmd, XPkgInternalException e) {
        if (e.getMessage().equalsIgnoreCase("arglen"))
            return new XPkgArgLenException(cmd, 2, "a variable is the second argument", e);
        else if (e.getMessage().equalsIgnoreCase("mismatch"))
            return new XPkgTypeMismatchException(cmd, "second", VarType.STRING, (VarType) e.getData(), e);
        return new XPkgUnimplementedException("An unknown exception occurred while handling ParseHelper.getStr()", e);
    }

    /**
     * This static method handles any {@link net.xpkgclient.exceptions.XPkgInternalException} that is thrown by {@link ExecutionContext#checkExistingVar(String)}.
     *
     * @param cmd  The command that is handling the exception thrown.
     * @param argC The ordinal index (first, second, third, fourth, etc.) of the argument which was supposed to be a variable.
     * @param e    The {@link net.xpkgclient.exceptions.XPkgInternalException} thrown.
     * @return A clear human-readable exception.
     */
    public static XPkgException handleCheckExistingVar(CommandName cmd, String argC, XPkgInternalException e) {
        String varName = (String) e.getData();
        if (e.getMessage().equalsIgnoreCase("invalid"))
            return new XPkgInvalidVarNameException(cmd, argC, varName, e);
        else if (e.getMessage().equalsIgnoreCase("undef"))
            return new XPkgUndefinedVarException(varName, e);
        return new XPkgUnimplementedException("An unknown exception occurred while handling ExecutionContext.handleCheckExistingVar()", e);
    }
}
