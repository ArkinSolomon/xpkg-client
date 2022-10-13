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

import java.net.URL;

/**
 * Exception thrown when there is an error fetching from remote.
 */
public class XPkgFetchException extends XPkgException {

    /**
     * Create a new exception with a message saying that the request failed.
     *
     * @param url The URL that we could not fetch successfully.
     */
    public XPkgFetchException(URL url){
        this(url, null);
    }

    /**
     * Create a new exception with a message saying that the request failed, and that another exception caused this one.
     *
     * @param url The URL that we could not get.
     * @param cause The exception that caused this one.
     */
    public XPkgFetchException(URL url, Throwable cause){
        super("Request failed: Request to '" + url + "' failed", cause);
    }
}
