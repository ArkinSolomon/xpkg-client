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

import java.io.Serial;

// Exception thrown if variable is not a pathlike when expected
public class XPkgNotPathLikeException extends XPkgRuntimeException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = -4254373123050488699L;

    //Say variable was not pathlike
    public XPkgNotPathLikeException(CommandName cmd, String varName, String value) {
        super(cmd + " expected the variable '" + varName + "' to be a pathlike string, got: " + value);
    }

    //Say constant was not path like
    public XPkgNotPathLikeException(CommandName cmd, String value) {
        super(cmd + " expected a pathlike, got: " + value);
    }

}
