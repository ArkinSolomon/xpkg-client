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

package net.xpkgclient.versioning;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class is used to store a single set of a version selection.
 */
public final class VersionRange implements Comparable<VersionRange>, Cloneable {

    /**
     * The maximum version number.
     *
     * @return The maximum version number.
     */
    @Getter
    private long maxVersionNum;

    /**
     * The minimum version number.
     *
     * @return The maximum version number.
     */
    @Getter
    private long minVersionNum;

    /**
     * The maximum version object.
     *
     * @return The maximum version object.
     */
    @Getter
    private Version maxVersion;

    /**
     * The minimum version object.
     *
     * @return The maximum version object.
     */
    @Getter
    private Version minVersion;

    /**
     * Create a new range set from two numbers.
     *
     * @param minVersion The minimum version of the range.
     * @param maxVersion The maximum version of the range.
     */
    public VersionRange(Version minVersion, Version maxVersion) {
        setMin(minVersion);
        setMax(maxVersion);
    }

    /**
     * Try merging two version ranges. Note that {@code r1} must be less than or equal to {@code r2}.
     *
     * @param r1 The first range to try merging.
     * @param r2 The second range to try merging.
     * @return Null if the two range sets can not be merged, {@code r1} if {@code r2} fits inside {@code r1}, or a new merged range set.
     */
    public static VersionRange tryMerge(VersionRange r1, VersionRange r2) {
        if (r1.compareTo(r2) > 0)
            throw new RuntimeException("r1 must be less than r2 in order to merge");

        if (r1.maxVersionNum < r2.minVersionNum)
            return null;

        if (r1.maxVersionNum < r2.maxVersionNum)
            return new VersionRange(r1.minVersion, r2.maxVersion);

        return r1;
    }

    /**
     * Create a deep copy of a list of version ranges.
     *
     * @param ranges The ranges to create a deep copy of.
     * @returns A new list that is a deep copy of all the ranges.
     */
    public static List<VersionRange> deepCopy(List<VersionRange> ranges) {
        return ranges.stream().map(VersionRange::clone).toList();
    }

    /**
     * Simplify the list of ranges.
     *
     * @param ranges The ranges to simplify.
     * @return A new deep copy of the ranges simplified.
     */
    public static List<VersionRange> simplify(List<VersionRange> ranges) {

        // Create a deep copy of the list
        ranges = deepCopy(ranges);

        int curr = 0;
        while (ranges.size() > 1 && curr < ranges.size() - 1) {
            VersionRange r = VersionRange.tryMerge(ranges.get(curr), ranges.get(curr + 1));
            if (r == null)
                ++curr;
            else {
                ranges.set(curr, r);
                ranges.remove(curr + 1);
            }
        }

        return ranges;
    }

    /**
     * Change the minimum version.
     *
     * @param minVersion The new minimum version.
     */
    public void setMin(Version minVersion) {
        this.minVersion = minVersion;
        this.minVersionNum = minVersion.getVersionNum();
    }

    /**
     * Change the maximum version.
     *
     * @param maxVersion The new maximum version.
     */
    public void setMax(Version maxVersion) {
        this.maxVersion = maxVersion;
        this.maxVersionNum = maxVersion.getVersionNum();
    }

    /**
     * Determine if this range set is equal to another.
     *
     * @param other The other range set to check for equality.
     * @return True if the min and max ranges are equal.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;

        VersionRange rangeSet = (VersionRange) other;

        if (maxVersionNum != rangeSet.maxVersionNum)
            return false;
        return minVersionNum == rangeSet.minVersionNum;
    }

    /**
     * Generate a unique hash code for this range set and all equal sets.
     *
     * @return A unique hash code for this range set.
     */
    @Override
    public int hashCode() {
        int result = (int) (maxVersionNum ^ (maxVersionNum >>> 32));
        result = 31 * result + (int) (minVersionNum ^ (minVersionNum >>> 32));
        result = 31 * result + (maxVersion != null ? maxVersion.hashCode() : 0);
        result = 31 * result + (minVersion != null ? minVersion.hashCode() : 0);
        return result;
    }

    /**
     * Compare this range set with another.
     *
     * @param other The set to be compared.
     * @return A zero if these two sets are equal, a negative number if this range set is less than {@code other}, or a positive number otherwise.
     */
    @Override
    public int compareTo(@NotNull VersionRange other) {
        if (equals(other))
            return 0;
        if (minVersionNum < other.minVersionNum)
            return -1;
        if (minVersionNum > other.minVersionNum)
            return 1;

        return maxVersion.compareTo(other.maxVersion);
    }

    /**
     * Get the string representation of this range.
     *
     * @return The string representation of this range.
     */
    @Override
    public String toString() {
        return "%s-%s".formatted(minVersion, maxVersion);
    }

    /**
     * True if this version range completely contains another version range.
     *
     * @param other The other version range to check.
     * @return True if this version range completely contains {@code other}.
     */
    public boolean containsRange(@NotNull VersionRange other) {
        return minVersionNum <= other.minVersionNum && maxVersionNum >= other.maxVersionNum;
    }

    /**
     * Create a deep copy of this version range.
     *
     * @return A new copy of this version range.
     */
    @Override
    @SneakyThrows(CloneNotSupportedException.class)
    public VersionRange clone() {
        VersionRange clone = (VersionRange) super.clone();
        clone.setMin(minVersion.clone());
        clone.setMax(maxVersion.clone());
        return clone;
    }
}