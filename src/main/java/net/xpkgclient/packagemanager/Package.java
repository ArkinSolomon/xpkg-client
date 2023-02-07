/*
 * Copyright (c) 2022-2023. XPkg-Client Contributors.
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
    private final Version latestVersion;

    /**
     * The latest version of the package (as a string so that JavaFX can access it when creating rows).
     *
     * @return The latest version of the package as a string.
     */
    @Getter
    private final String latestVersionStr;

    /**
     * The package description.
     *
     * @return The package description.
     */
    @Getter
    private final String description;

    /**
     * The package author's name.
     *
     * @return The package author's name.
     */
    @Getter
    private final String authorName;

    /**
     * The package author's id.
     *
     * @return The package author's id.
     */
    @Getter
    private final String authorId;

    /**
     * The type of the package.
     *
     * @return The type of the package.
     */
    @Getter
    private final PackageType packageType;

    /**
     * All the (published) versions that a package has..
     *
     * @return All the (published) versions that a package has.
     */
    @Getter
    Version[] versions;

    /**
     * @param packageId      The package identifier.
     * @param packageName    The name of the package.
     * @param packageType  The type of the package.
     * @param versionStrings The versions of the package as version strings.
     * @param description    The package's description.
     * @param authorName         The package author's name.
     * @param authorId The id of the author.
     */
    public Package(String packageId, String packageName, PackageType packageType, @NotNull String[] versionStrings, String description, String authorName, String authorId) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.packageType = packageType;
        this.description = description;
        this.authorName = authorName;
        this.authorId = authorId;

        this.versions = Arrays.stream(versionStrings).map(versionStr -> {
            try {
                return new Version(versionStr);
            } catch (InvalidVersionException e) {
                throw new RuntimeException(e);
            }
        }).toArray(Version[]::new);

        List<Version> sortedVersions = new ArrayList<>(List.of(this.versions));
        sortedVersions.sort(Comparator.comparingInt(Version::getMajor)
                .thenComparingInt(Version::getMinor)
                .thenComparingInt(Version::getPatch));
        latestVersion = sortedVersions.get(versionStrings.length - 1);
        latestVersionStr = latestVersion.toString();
    }
}
