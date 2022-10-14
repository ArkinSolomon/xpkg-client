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

import net.xpkgclient.packagemanager.Version;
import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown when a version string is invalid.
 */
public class XPkgInvalidVersionException extends XPkgException {

    /**
     * Create a new exception saying that the version is invalid.
     *
     * @param version The version that is invalid.
     */
    public XPkgInvalidVersionException(@NotNull Version version) {
        this(version.toString());
    }

    /**
     * Create a new exception saying that the version string is invalid.
     *
     * @param version The version string that was invalid.
     */
    public XPkgInvalidVersionException(String version) {
        this(version, null);
    }

    /**
     * Create a new exception saying that the version string is invalid, and that another exception caused this one.
     *
     * @param version The version string that was invalid.
     * @param cause   The exception that caused this one.
     */
    public XPkgInvalidVersionException(String version, Throwable cause) {
        super("Invalid version string: The version string: '" + version + "' is invalid and can not be parsed", cause);
    }
}
