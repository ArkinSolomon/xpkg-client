/*
 * Copyright (c) 2023. Arkin Solomon.
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

package net.xpkgclient.versioning;

/**
 * An exception thrown when a version is invalid.
 */
public class InvalidVersionException extends Exception {

    /**
     * Say that a version is not valid.
     *
     * @param version The version that is not valid.
     */
    public InvalidVersionException(String version){
        this(version, null);
    }

    /**
     * Say that a version is not valid, and that this exception was caused by another one.
     *
     * @param version The version that is not valid.
     * @param cause The exception that caused this one.
     */
    public InvalidVersionException(String version, Throwable cause){
        super("The version \"%s\" is not valid".formatted(version), cause);
    }
}
