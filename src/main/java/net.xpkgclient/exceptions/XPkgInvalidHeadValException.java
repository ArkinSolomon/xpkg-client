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

import net.xpkgclient.enums.HeadKey;

import java.io.Serial;

// Exception thrown if a value is not suitable for a head key
public class XPkgInvalidHeadValException extends XPkgParseException {

    //Serial identifier
    @Serial
    private static final long serialVersionUID = 6072185362526976813L;

    // Basic constructor to show a simple message
    public XPkgInvalidHeadValException(HeadKey hk, String invalidVal) {
        super("Invalid head value: '" + invalidVal + "' is not a valid value for " + hk);
    }
}
