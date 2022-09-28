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

import java.io.Serial;

// The top level class for exceptions while parsing, like invalid scripts
public class XPkgParseException extends XPkgScriptRunnerException implements ILineException<XPkgParseException> {

    // Serial identifier
    @Serial
    private static final long serialVersionUID = 6594331598568622239L;

    //Basic class constructors
    public XPkgParseException() {
    }

    public XPkgParseException(String message) {
        super(message);
    }

    public XPkgParseException(String message, Exception cause) {
        super(message, cause);
    }

    public XPkgParseException(Exception cause) {
        super(cause);
    }

    private XPkgParseException(int line, XPkgParseException e) {
        this("Parse error at line " + line + ": " + e.getMessage(), e);
    }

    @Override
    public XPkgParseException setLine(int line) {
        return new XPkgParseException(line, this);
    }
}
