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
 * Exceptions meant to be caught in order to be thrown later (useful for full stack trace debugging), used mainly in static classes and passing information.
 */
public class XPkgInternalException extends Exception {

    // Data held by this exception for passing up secondary information.
    Object data;

    // Basic constructors

    /**
     * Create an exception with no message.
     */
    public XPkgInternalException() {
        this(null, null);
    }

    /**
     * Create an exception with a message.
     *
     * @param message The message for the exception.
     */
    public XPkgInternalException(String message) {
        this(message, null);
    }

    /**
     * Create an exception with a message and secondary data.
     *
     * @param message The message for the exception.
     * @param data    Additional data for the exception.
     */
    public XPkgInternalException(String message, Object data) {
        super(message);
        this.data = data;
    }

    /**
     * Create an exception with a message and a cause.
     *
     * @param message The message for the exception.
     * @param cause   The cause of the exception.
     */
    public XPkgInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new exception with no message and a cause.
     *
     * @param cause The cause of the exception.
     */
    public XPkgInternalException(Throwable cause) {

        // We want to call our own constructor to set the message of the exception
        this(cause == null ? null : cause.toString(), cause);
        if (cause instanceof XPkgInternalException)
            data = ((XPkgInternalException) cause).getData();
    }

    /**
     * Get the data passed in, returns null if there was no data.
     *
     * @return The data passed in.
     */
    public Object getData() {
        return data;
    }

    /**
     * Set the data of this exception.
     *
     * @param data The data to pass through the exception.
     */
    public void setData(Object data) {
        this.data = data;
    }

}
