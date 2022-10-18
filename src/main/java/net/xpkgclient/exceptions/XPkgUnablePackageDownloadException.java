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

import net.xpkgclient.packagemanager.Package;
import net.xpkgclient.packagemanager.Version;
import org.jetbrains.annotations.NotNull;

/**
 * Exception thrown if we can't download a package for any reason.
 */
public class XPkgUnablePackageDownloadException extends XPkgException {


    /**
     * Create a new exception saying that a package at a specific version could not be downloaded.
     *
     * @param pkg     The package that could not be downloaded.
     * @param version The version of the package {@code pkg} that could not be downloaded.
     */
    public XPkgUnablePackageDownloadException(Package pkg, Version version) {
        this(pkg, version, null);
    }

    /**
     * Create a new exception saying that a package at a specific version could not be downloaded, and that an exception caused this one.
     *
     * @param pkg     The package that could not be downloaded.
     * @param version The version of the package {@code pkg} that could not be downloaded.
     * @param cause   The exception that caused this one.
     */
    public XPkgUnablePackageDownloadException(@NotNull Package pkg, @NotNull Version version, Throwable cause) {
        super("Could not download package: The package " + pkg.getPackageId() + "@" + version + " [" + pkg.getPackageName() + "] could not be downloaded", cause);
    }
}
