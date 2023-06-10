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

package net.xpkgclient.packagemanager;

import net.xpkgclient.versioning.Version;

/**
 * An exception thrown if a version of a package is already installed.
 */
public class VersionAlreadyInstalledException extends Exception {

    /**
     * Create a new exception saying that a package at a specific version is already installed.
     *
     * @param packageId The id of the package that is already installed.
     * @param version   The version of the package that is already installed.
     */
    public VersionAlreadyInstalledException(String packageId, Version version) {
        this(packageId, version, null);
    }

    /**
     * Create a new exception saying that a package at a specific version is already installed, and that another exception caused this one.
     *
     * @param packageId The id of the package that is already installed.
     * @param version   The version of the package that is already installed.
     * @param cause     The throwable or exception that caused this one.
     */
    public VersionAlreadyInstalledException(String packageId, Version version, Throwable cause) {
        super("The package %s@%s is already installed".formatted(packageId, version), cause);
    }
}
