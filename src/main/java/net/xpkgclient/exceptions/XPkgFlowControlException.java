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

/**
 * Exception thrown when a user makes an issue with flow control.
 */
public class XPkgFlowControlException extends XPkgParseException {

    /**
     * Create a new exception with a message.
     *
     * @param message The message for the exception.
     */
    public XPkgFlowControlException(String message) {
        super("Flow control error: " + message);
    }
}
