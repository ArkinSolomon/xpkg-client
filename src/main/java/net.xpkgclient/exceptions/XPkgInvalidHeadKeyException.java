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

// An exception thrown if a head key is invalid
public class XPkgInvalidHeadKeyException extends XPkgParseException {

    //Serial identifier
    @Serial
    private static final long serialVersionUID = -6211878735670435376L;

    // Basic constructor to show a simple message
    public XPkgInvalidHeadKeyException(String invalidKey) {
        super("Invalid head key: '" + invalidKey + "' is not a valid head key");
    }
}
