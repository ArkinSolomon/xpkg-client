/*
 * Copyright (c) 2022-2023. Arkin Solomon.
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

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A class to compare different versions and version strings.
 */
public final class Version implements Comparable<Version> {

    public static final Version MIN_VERSION;
    public static final Version MAX_VERSION;

    static {
        try {
            MIN_VERSION = new Version(0, 0, 1, 'a', 1);
            MAX_VERSION = new Version(999, 999, 999);
        } catch (InvalidVersionException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * The major number of this version.
     *
     * @param major The new major of this version.
     * @return The major number of this version.
     */
    @Getter
    @Setter
    private int major;

    /**
     * The minor number of this version.
     *
     * @param minor The new minor version number
     * @return The minor number of this version.
     */
    @Getter
    @Setter
    private int minor = 0;

    /**
     * The patch number of this version.
     *
     * @param patch The new patch number of this version.
     * @return The patch number of this version.
     */
    @Getter
    @Setter
    private int patch = 0;

    /**
     * This char is 'a' if this is an alpha version, 'b' if it is a beta version, or null if it's a release version.
     *
     * @return A char which is 'a' if this is an alpha version, 'b' if it is a beta version, or null if it's a release version.
     */
    @Getter
    private Character alphaOrBeta = null;

    /**
     * Pre-release number. Null if {@code alphaOrBeta} is null.
     *
     * @return The pre-release number, or null if {@code alphaOrBeta} is null.
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
     * Create a version object from a version string, except don't throw an error if the version is invalid.
     *
     * @param versionStr The version string.
     * @return A version object representation of {@code versionStr}, or {@code null} if {@code versionStr} is invalid.
     */
    public static Version fromString(String versionStr) {
        try {
            return new Version(versionStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Turn {@code num} into a string and return as a 3-digit (character) string, prepending zeros.
     *
     * @param num The number to turn to a three-digit string.
     * @return The number as a three-digit string.
     */
    private static String toThreeDigits(int num) {
        String numStr = String.valueOf(num);

        if (numStr.length() == 1)
            numStr = "00" + numStr;
        else if (numStr.length() == 2)
            numStr = "0" + numStr;
        else if (numStr.length() != 3)
            throw new UnsupportedOperationException("Can not convert an integer that is longer than three digits to a three-digit string.");

        return numStr;
    }

    /**
     * Change the pre-release number. Only update if {@link Version#alphaOrBeta} is set.
     *
     * @param preReleaseVersion The new pre-release version number.
     */
    private void setPreReleaseVersion(Integer preReleaseVersion) {
        if (isPreRelease())
            this.preReleaseVersion = preReleaseVersion;

    }

    /**
     * Set the new alpha or beta version. Use 'a' for alpha, or 'b' for beta, or null if it's a release version. Sets {@link Version#preReleaseVersion} to 1 if it is zero or not set.
     *
     * @param alphaOrBeta The new alpha or beta version.
     */
    public void setAlphaOrBeta(Character alphaOrBeta) throws InvalidVersionException {
        if (alphaOrBeta != null && alphaOrBeta != 'a' && alphaOrBeta != 'b')
            throw new InvalidVersionException("Alpha or beta version must be set by either \"a\" or \"b\" or must be null, not \"%c\"".formatted(alphaOrBeta));

        this.alphaOrBeta = alphaOrBeta;
        if (this.preReleaseVersion == null || this.preReleaseVersion == 0)
            this.preReleaseVersion = 1;
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

    /**
     * Create a copy of this version.
     *
     * @return A copy of this version.
     */
    @SneakyThrows
    public Version copy() {
        if (!isPreRelease())
            return new Version(major, minor, patch);
        return new Version(major, minor, patch, alphaOrBeta, preReleaseVersion);
    }

    /**
     * Compute and retrieve the number representation of this version.
     *
     * @return The number representation of this version.
     */
    public long getVersionNum() {
        String longStr = "%s%s%s000000".formatted(major, toThreeDigits(minor), toThreeDigits(patch));

        long semverNum = Long.parseLong(longStr);
        if (!isPreRelease()){
            return semverNum;
        }

        int preReleaseNum = 999 - preReleaseVersion;
        String preReleaseFloatStr;
        if (alphaOrBeta == 'a')
            preReleaseFloatStr = "999%s".formatted(toThreeDigits(preReleaseNum));
        else
            preReleaseFloatStr = "%s999".formatted(toThreeDigits(preReleaseNum));
        long preReleaseFloat = Long.parseLong(preReleaseFloatStr);

        return semverNum - preReleaseFloat;
    }

    /**
     * Compare two versions together.
     *
     * @param other The other version to be compared.
     * @return Zero if {@code other} is equal to this version, a negative value if {@code other} is greater than this version, or a positive otherwise.
     */
    @Override
    public int compareTo(@NotNull Version other) {
        return Long.compare(getVersionNum(), other.getVersionNum());
    }
}
