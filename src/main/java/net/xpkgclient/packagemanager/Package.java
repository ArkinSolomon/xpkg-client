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

package net.xpkgclient.packagemanager;

import lombok.Getter;

/**
 * An instance of this class represents a single package.
 */
public final class Package {

    /**
     * The package identifier.
     *
     * @return The package identifier.
     */
    @Getter
    private final String packageId;

    /**
     * The name of the package.
     *
     * @return The name of the package.
     */
    @Getter
    private final String packageName;

    /**
     * The version of the package
     *
     * @return The version of the package.
     */
    @Getter
    private final String version;

    /**
     * The package description.
     *
     * @return The package description.
     */
    @Getter
    private final String description;

    /**
     * The package author.
     *
     * @return The package author.
     */
    @Getter
    private final String author;

    /**
     * @param packageId   The package identifier.
     * @param packageName The name of the package.
     * @param version     The version of the package.
     * @param description The package description.
     * @param author      The package author.
     */
    public Package(String packageId, String packageName, String version, String description, String author) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.version = version;
        this.description = description;
        this.author = author;
    }
}
