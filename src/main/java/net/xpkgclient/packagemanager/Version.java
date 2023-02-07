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

import java.util.Objects;

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
     * This char is 'a' if this is an alpha version, 'b' if it is a beta version, or null if it's a release version.
     *
     * @return A char which is 'a' if this is an alpha version, 'b' if it is a beta version, or null if it's a release version.
     */
    @Getter
    private Character alphaOrBeta = null;

    /**
     * Pre-release version. Null if {@code alphaOrBeta} is null.
     *
     * @return The Pre-release version, or null if {@code alphaOrBeta} is null.
     */
    @Getter
    private Integer preReleaseVersion = null;

    /**
     * Parse a version string in the form of {@code major.minor.patch}.
     *
     * @param versionStr The string to parse.
     * @throws InvalidVersionException Exception thrown if the version string is invalid.
     */
    public Version(@NotNull String versionStr) throws InvalidVersionException {
        versionStr = versionStr.toLowerCase();
        if (versionStr.length() < 1 || versionStr.length() > 15)
            throw new InvalidVersionException(versionStr);

        String semanticPart = versionStr;

        final boolean containsA = versionStr.contains("a");
        final boolean containsB = versionStr.contains("b");
        if (containsA || containsB) {
            alphaOrBeta = containsA ? 'a' : 'b';

            String[] parts = versionStr.split(String.valueOf(alphaOrBeta), -1);
            if (parts.length != 2)
                throw new InvalidVersionException(versionStr);

            semanticPart = parts[0];
            String preReleasePart = parts[1];

            try {
                preReleaseVersion = Integer.parseInt(preReleasePart, 10);
            } catch (Throwable e) {
                throw new InvalidVersionException(versionStr, e);
            }
        }

        // Parse the semantic part
        String[] partsStr = semanticPart.split("\\.", -1);
        if (partsStr.length > 3 || partsStr.length == 0)
            throw new InvalidVersionException(versionStr);

        for (final String part : partsStr) {
            if (part.isBlank())
                throw new InvalidVersionException(versionStr);
        }

        try {
            major = Integer.parseInt(partsStr[0], 10);
            if (partsStr.length > 1)
                minor = Integer.parseInt(partsStr[1], 10);
            if (partsStr.length > 2)
                patch = Integer.parseInt(partsStr[2], 10);
        } catch (Throwable e) {
            throw new InvalidVersionException(versionStr, e);
        }
        checkVersionValidity(versionStr);
    }

    /**
     * Create a new version from only the major number, set the minor and patch numbers to zero.
     *
     * @param major The major number of the version.
     * @throws InvalidVersionException Exception thrown if the version is not valid.
     */
    public Version(int major) throws InvalidVersionException {
        this.major = major;
        checkVersionValidity();
    }

    /**
     * Create a new version from the major and minor numbers, and set the patch number to zero.
     *
     * @param major The major number of the version.
     * @param minor The minor number of the version.
     * @throws InvalidVersionException Exception thrown if the version is not valid.
     */
    public Version(int major, int minor) throws InvalidVersionException {
        this.major = major;
        this.minor = minor;
        checkVersionValidity();
    }

    /**
     * Create a new version from the major, minor, and patch numbers.
     *
     * @param major The major number of the version.
     * @param minor The minor number of the version.
     * @param patch The patch number of the version.
     * @throws InvalidVersionException Exception thrown if the version is not valid.
     */
    public Version(int major, int minor, int patch) throws InvalidVersionException {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        checkVersionValidity();
    }

    /**
     * Create a new pre-release version from the major, minor, and patch numbers.
     *
     * @param major         The major number of the version.
     * @param minor         The minor number of the version.
     * @param patch         The patch number of the version.
     * @param alphaOrBeta   Whether this version is an alpha or beta prerelease.
     * @param preReleaseNum The pre-release version.
     * @throws InvalidVersionException Exception thrown if the version is not valid.
     */
    public Version(int major, int minor, int patch, char alphaOrBeta, int preReleaseNum) throws InvalidVersionException {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.alphaOrBeta = alphaOrBeta;
        this.preReleaseVersion = preReleaseNum;
        checkVersionValidity();
    }

    /**
     * Check if this version represents a pre-release version.
     *
     * @return True if this version represents a pre-release version.
     */
    public boolean isPreRelease() {
        return alphaOrBeta != null;
    }

    /**
     * Check to make sure that a version is valid, which means that the major minor and version numbers are greater than or equal zero, and that all three of them do not equal zero. Create a new version string for the exception if it is not.
     *
     * @throws InvalidVersionException Exception thrown if this version is not valid.
     */
    private void checkVersionValidity() throws InvalidVersionException {
        checkVersionValidity(null);
    }

    /**
     * Check to make sure that a version is valid, which means that the major minor and version numbers are greater than or equal zero, and that all three of them do not equal zero. Use the provided version string for the exception if it is not. If the provided version string is null, create a new one for the exception.
     *
     * @param v The version string that was provided at creation.
     * @throws InvalidVersionException Exception thrown if this version is not valid.
     */
    private void checkVersionValidity(String v) throws InvalidVersionException {
        if (major < 0 ||
                minor < 0 ||
                patch < 0 ||
                major > 999 ||
                minor > 999 ||
                patch > 999 ||
                (major | minor | patch) == 0 ||
                (isPreRelease() && (preReleaseVersion > 999 || preReleaseVersion < 1)))
            throw new InvalidVersionException(v == null ? toString() : v);
    }

    /**
     * Get the string representation of this version in the form {@code major.minor.patch}.
     *
     * @return The string representation of this version
     */
    @Override
    public String toString() {
        if (isPreRelease())
            return String.format("%d.%d.%d%c%d", major, minor, patch, alphaOrBeta, preReleaseVersion);
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

        if (alphaOrBeta != null &&
                (!alphaOrBeta.equals(version.alphaOrBeta) ||
                        !preReleaseVersion.equals(version.preReleaseVersion))) {
            return false;
        }

        return major == version.major &&
                minor == version.minor &&
                patch == version.patch;
    }

    /**
     * Generate a unique identifier.
     *
     * @return The unique identifier.
     */
    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, alphaOrBeta, preReleaseVersion);
    }
}
