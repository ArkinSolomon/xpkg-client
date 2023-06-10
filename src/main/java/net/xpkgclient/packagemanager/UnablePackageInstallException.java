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
 * An exception thrown when a package can not be installed.
 */
public class UnablePackageInstallException extends Exception {

    /**
     * Create an exception saying that a package could not be installed.
     *
     * @param packageId The id of the package that could not be installed.
     * @param version   The version of the package that could not be installed.
     */
    public UnablePackageInstallException(String packageId, Version version) {
        this(packageId, version, null);
    }

    /**
     * Create an exception saying that a package could not be installed, and that another exception caused this one.
     *
     * @param packageId The id of the package that could not be installed.
     * @param version   The version of the package that could not be installed.
     * @param cause     The exception that caused this one.
     */
    public UnablePackageInstallException(String packageId, Version version, Throwable cause) {
        super("Can not install %s@%s".formatted(packageId, version), cause);
    }
}
