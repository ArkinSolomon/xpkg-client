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

// Used when argument lengths do not match up
public class XPkgArgLenException extends XPkgParseException {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = 4663186103057388801L;

    // Basic argument length mismatch
    public XPkgArgLenException(CommandName cmd, int expected, int actual) {
        super("Invalid argument length: The " + cmd + " command expects " + expected + " arguments, got " + actual);
    }

    // Too many arguments with a note for a reason
    public XPkgArgLenException(CommandName cmd, int max, String reason) {
        super("Invalid argument length: The " + cmd + " command expects " + max + " arguments if  " + reason);
    }

    // Command takes no arguments
    public XPkgArgLenException(CommandName cmd, int given) {
        super("Invalid argument length: The " + cmd + " command takes no arguments, but " + given + " were given");
    }

    //Add line based exception
    private XPkgArgLenException(int line, XPkgArgLenException e) {
        super("Parse error at line " + line + ": " + e.getMessage(), e);
    }

    @Override
    public XPkgArgLenException setLine(int line) {
        return new XPkgArgLenException(line, this);
    }
}
