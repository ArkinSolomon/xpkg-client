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
 * An exception thrown when a package is not installed.
 */
public class PackageNotInstalledException extends Exception {

    /**
     * Create a new exception saying that the package was not installed.
     *
     * @param packageId The id of the package that was not installed.
     */
    public PackageNotInstalledException(String packageId) {
        this(packageId, (Throwable) null);
    }

    /**
     * Create a new exception saying that the package was not installed, and that another exception caused this one.
     *
     * @param packageId The id of the package that was not installed.
     * @param cause     The exception that caused this one.
     */
    public PackageNotInstalledException(String packageId, Throwable cause) {
        super("No version of %s is installed".formatted(packageId), cause);
    }

    /**
     * Create a new exception saying that a package at a specific version was not installed.
     *
     * @param packageId The id of the package that was not installed.
     * @param version   The version of the package that was not installed.
     */
    public PackageNotInstalledException(String packageId, Version version) {
        this(packageId, version, null);
    }

    /**
     * Create a new exception saying that a package at a specific version was not installed, and that another exception caused this one.
     *
     * @param packageId The id of the package that was not installed.
     * @param version   The version of the package that was not installed.
     * @param cause     The exception that caused this one.
     */
    public PackageNotInstalledException(String packageId, Version version, Throwable cause) {
        super("The package %s@%s is not installed".formatted(packageId, version), cause);
    }
}
