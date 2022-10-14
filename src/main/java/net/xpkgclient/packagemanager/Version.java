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
import net.xpkgclient.exceptions.XPkgInvalidVersionException;
import org.jetbrains.annotations.NotNull;

/**
 * A class to compare different versions and version strings.
 */
public final class Version {

    /**
     * The major number of this version.
     *
     * @return The major number of this version.
     */
    @Getter
    private final int major;

    /**
     * The minor number of this version.
     *
     * @return The minor number of this version.
     */
    @Getter
    private int minor = 0;

    /**
     * The patch number of this version.
     *
     * @return The patch number of this version.
     */
    @Getter
    private int patch = 0;

    /**
     * Parse a version string in the form of {@code major.minor.patch}.
     *
     * @param versionStr The string to parse.
     * @throws XPkgInvalidVersionException Exception thrown if the version string is invalid
     */
    public Version(@NotNull String versionStr) throws XPkgInvalidVersionException {
        String[] partsStr = versionStr.split("\\.", -1);
        if (partsStr.length > 3 || partsStr.length == 0)
            throw new XPkgInvalidVersionException(versionStr);

       for (final String part : partsStr) {
           if (part.isBlank())
               throw new XPkgInvalidVersionException(versionStr);
       }

        try {
            major = Integer.parseInt(partsStr[0]);
            if (partsStr.length > 1)
                minor = Integer.parseInt(partsStr[1]);
            if (partsStr.length > 2)
                patch = Integer.parseInt(partsStr[2]);
        } catch (Throwable e) {
            throw new XPkgInvalidVersionException(versionStr, e);
        }
        checkVersionValid(versionStr);
    }

    /**
     * Create a new version from only the major number, set the minor and patch numbers to zero.
     *
     * @param major The major number of the version.
     */
    public Version(int major) throws XPkgInvalidVersionException {
        this.major = major;
        checkVersionValid();
    }

    /**
     * Create a new version from the major and minor numbers, and set the patch number to zero.
     *
     * @param major The major number of the version.
     * @param minor The minor number of the version.
     */
    public Version(int major, int minor) throws XPkgInvalidVersionException {
        this.major = major;
        this.minor = minor;
        checkVersionValid();
    }

    /**
     * Create a new version from the major, minor, and patch numbers.
     *
     * @param major The major number of the version.
     * @param minor The minor number of the version.
     * @param patch The patch number of the version.
     */
    public Version(int major, int minor, int patch) throws XPkgInvalidVersionException {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        checkVersionValid();
    }

    /**
     * Check to make sure that a version is valid, which means that the major minor and version numbers are greater than or equal zero, and that all three of them do not equal zero. Create a new version string for the exception if it is not.
     *
     * @throws XPkgInvalidVersionException Exception thrown if this version is not valid.
     */
    private void checkVersionValid() throws XPkgInvalidVersionException {
        checkVersionValid(null);
    }

    /**
     * Check to make sure that a version is valid, which means that the major minor and version numbers are greater than or equal zero, and that all three of them do not equal zero. Use the provided version string for the exception if it is not. If the provided version string is null, create a new one for the exception.
     *
     * @param v The version string that was provided at creation.
     * @throws XPkgInvalidVersionException Exception thrown if this version is not valid.
     */
    private void checkVersionValid(String v) throws XPkgInvalidVersionException {
        if (major < 0 || minor < 0 || patch < 0 || (major | minor | patch) == 0)
            throw new XPkgInvalidVersionException(v == null ? toString() : v);
    }

    /**
     * Get the string representation of this version in the form {@code major.minor.patch}.
     *
     * @return The string representation of this version
     */
    @Override
    public String toString() {
        return String.format("%d.%d.%d", major, minor, patch);
    }

    /**
     * Check if two versions are equal.
     *
     * @param o The reference object with which to compare.
     * @return True if the two versions have the same major minor and patch numbers.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version = (Version) o;
        return major == version.major && minor == version.minor && patch == version.patch;
    }
}
